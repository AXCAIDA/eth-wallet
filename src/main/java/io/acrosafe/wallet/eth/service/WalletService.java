/**
 * MIT License
 *
 * Copyright (c) 2020 acrosafe technologies
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.acrosafe.wallet.eth.service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import io.acrosafe.wallet.core.eth.Token;
import io.acrosafe.wallet.eth.domain.TransactionRecord;
import io.acrosafe.wallet.eth.repository.TransactionRecordRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.acrosafe.wallet.core.eth.CryptoUtils;
import io.acrosafe.wallet.core.eth.ETHAccount;
import io.acrosafe.wallet.core.eth.ETHWallet;
import io.acrosafe.wallet.core.eth.IDGenerator;
import io.acrosafe.wallet.core.eth.Passphrase;
import io.acrosafe.wallet.core.eth.SeedGenerator;
import io.acrosafe.wallet.core.eth.exception.AccountNotFoundException;
import io.acrosafe.wallet.core.eth.exception.ContractCreationException;
import io.acrosafe.wallet.core.eth.exception.CryptoException;
import io.acrosafe.wallet.core.eth.exception.InvalidCredentialException;
import io.acrosafe.wallet.core.eth.exception.WalletNotFoundException;
import io.acrosafe.wallet.eth.config.ApplicationProperties;
import io.acrosafe.wallet.eth.domain.AddressRecord;
import io.acrosafe.wallet.eth.domain.WalletRecord;
import io.acrosafe.wallet.eth.exception.InvalidCoinSymbolException;
import io.acrosafe.wallet.eth.exception.InvalidEnterpriseAccountException;
import io.acrosafe.wallet.eth.exception.InvalidPassphraseException;
import io.acrosafe.wallet.eth.exception.InvalidWalletNameException;
import io.acrosafe.wallet.eth.repository.AddressRecordRepository;
import io.acrosafe.wallet.eth.repository.WalletRecordRepository;

@Service
public class WalletService
{
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    private static final String ETH_SYMBOL = "ETH";

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private SeedGenerator seedService;

    @Autowired
    private BlockChainService blockChainService;

    @Autowired
    private EnterpriseAccountCacheService enterpriseAccountCacheService;

    @Autowired
    private WalletCacheService walletCacheService;

    @Autowired
    private WalletRecordRepository walletRecordRepository;

    @Autowired
    private AddressRecordRepository addressRecordRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    private boolean isReady;

    @PostConstruct
    public void initialize()
    {
        addBlockChainListener();
    }

    @Transactional
    public String createAddress(String walletId) throws WalletNotFoundException, ContractCreationException
    {
        final ETHWallet wallet = this.walletCacheService.getWallet(walletId);

        final String id = IDGenerator.randomUUID().toString();
        AddressRecord addressRecord = new AddressRecord();
        addressRecord.setId(id);
        addressRecord.setWalletId(walletId);
        addressRecord.setCreatedDate(Instant.now());
        this.addressRecordRepository.save(addressRecord);

        logger.info("new address record {} is created.", id);
        this.blockChainService.deployAddressContract(id,
                wallet.getEnterpriseAccount().getCredentials(this.applicationProperties.getPassphrase()),
                wallet.getOwnerAccount().getAddress());

        return id;
    }

    @Transactional
    public WalletRecord createWallet(String symbol, String name, String label, Passphrase walletPassphrase,
            String enterpriseAccountId) throws InvalidCoinSymbolException, InvalidWalletNameException, InvalidPassphraseException,
            InvalidEnterpriseAccountException, AccountNotFoundException, CryptoException, InvalidCredentialException,
            ContractCreationException
    {
        if (StringUtils.isEmpty(symbol) || !symbol.equalsIgnoreCase(ETH_SYMBOL))
        {
            throw new InvalidCoinSymbolException("coin symbol is not valid.");
        }

        if (StringUtils.isEmpty(name))
        {
            throw new InvalidWalletNameException("wallet name is not valid.");
        }

        if (walletPassphrase == null || StringUtils.isEmpty(walletPassphrase.getStringValue()))
        {
            throw new InvalidPassphraseException("passphrase cannot be null or empty.");
        }

        if (StringUtils.isEmpty(enterpriseAccountId))
        {
            throw new InvalidEnterpriseAccountException("enterprise id cannot be null or empty.");
        }

        ETHAccount enterpriseAccount = this.enterpriseAccountCacheService.getEnterpriseAccount(enterpriseAccountId);
        if (enterpriseAccount == null)
        {
            throw new InvalidEnterpriseAccountException(
                    "failed to find enterprise account by given id. enterpriseAccountId = " + enterpriseAccountId);
        }

        // this.blockChainService.deployTestToken(enterpriseAccount.getCredentials(applicationProperties.getPassphrase()));

        final byte[] signerSeed = this.seedService.getSeed(this.applicationProperties.getServiceId(), 256, 256);
        final byte[] backupSeed = this.seedService.getSeed(this.applicationProperties.getServiceId(), 256, 256);

        final byte[] spec = CryptoUtils.generateIVParameterSpecBytes();
        final String encodedSpec = Base64.getEncoder().encodeToString(spec);
        final byte[] salt = CryptoUtils.generateSaltBytes();
        final String encodedSalt = Base64.getEncoder().encodeToString(salt);

        String encryptedSignerSeed = null;
        String encryptedBackupSeed = null;
        try
        {
            encryptedSignerSeed = CryptoUtils.encrypt(walletPassphrase.getStringValue(), signerSeed, spec, salt);
            encryptedBackupSeed = CryptoUtils.encrypt(walletPassphrase.getStringValue(), backupSeed, spec, salt);
        }
        catch (Throwable t)
        {
            // this shouldn't happen at all.
            throw new CryptoException("Invalid crypto operation.", t);
        }

        final String id = IDGenerator.randomUUID().toString();
        ETHAccount signerAccount =
                new ETHAccount(encryptedSignerSeed, spec, salt, this.applicationProperties.getTestnet(), walletPassphrase);
        ETHAccount backupAccount =
                new ETHAccount(encryptedBackupSeed, spec, salt, this.applicationProperties.getTestnet(), walletPassphrase);

        ETHWallet wallet = new ETHWallet(id, enterpriseAccount, signerAccount, backupAccount);
        logger.info("multisig wallet {} is created. addresses = {}", id, String.join(",", wallet.getSigningKeys()));

        WalletRecord walletRecord = new WalletRecord();
        walletRecord.setId(id);
        walletRecord.setEnterpriseAccountId(enterpriseAccountId);
        walletRecord.setName(name);
        walletRecord.setLabel(label);
        walletRecord.setCreatedDate(Instant.now());
        walletRecord.setSignerSeed(encryptedSignerSeed);
        walletRecord.setSignerAddress(signerAccount.getAddress());
        walletRecord.setBackupSeed(encryptedBackupSeed);
        walletRecord.setBackupAddress(backupAccount.getAddress());
        walletRecord.setSpec(encodedSpec);
        walletRecord.setSalt(encodedSalt);
        walletRecord.setEnabled(true);

        this.walletRecordRepository.save(walletRecord);

        this.blockChainService.deployWalletContract(wallet, id,
                enterpriseAccount.getCredentials(this.applicationProperties.getPassphrase()), wallet.getSigningKeys());

        return walletRecord;
    }

    public Map<String, BigInteger> getBalances(String walletId) throws WalletNotFoundException
    {
        final ETHWallet wallet = this.walletCacheService.getWallet(walletId);

        return this.blockChainService.getBalances(wallet.getAddress(), this.walletCacheService.getTokens());
    }

    public List<TransactionRecord> getTransactions(Token token, String walletId, int pageId, int size)
            throws WalletNotFoundException
    {
        ETHWallet wallet = this.walletCacheService.getWallet(walletId);

        Pageable pageable = PageRequest.of(pageId, size, Sort.by(Sort.Direction.ASC, "CreatedDate"));
        List<TransactionRecord> records = this.transactionRecordRepository.findAllByWalletIdAndToken(pageable, walletId, token);

        return records;
    }

    private void addBlockChainListener()
    {
        List<AddressRecord> addressRecords = this.addressRecordRepository.findAll();
        if (addressRecords != null && addressRecords.size() != 0)
        {
            for (AddressRecord addressRecord : addressRecords)
            {
                String address = addressRecord.getAddress();
                if (address != null)
                {
                    logger.info("retoring transactions for address {}", addressRecord.getAddress());
                    try
                    {
                        final String walletId = addressRecord.getWalletId();
                        this.blockChainService.subscribe(address, walletId);
                    }
                    catch (Throwable t)
                    {
                        // we will let it continue.
                        logger.error("failed to get deposit history for address {}", address, t);
                    }
                }
                else
                {
                    logger.info("address record {} doesn't have valid address.", address);
                }
            }
        }
    }
}
