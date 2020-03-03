package io.acrosafe.wallet.eth.service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import io.acrosafe.wallet.core.eth.BlockChainNetwork;
import io.acrosafe.wallet.core.eth.CryptoUtils;
import io.acrosafe.wallet.core.eth.ETHAccount;
import io.acrosafe.wallet.core.eth.IDGenerator;
import io.acrosafe.wallet.core.eth.SeedGenerator;
import io.acrosafe.wallet.core.eth.exception.AccountNotFoundException;
import io.acrosafe.wallet.core.eth.exception.CryptoException;
import io.acrosafe.wallet.core.eth.exception.GetBalanceException;
import io.acrosafe.wallet.core.eth.exception.InvalidCredentialException;
import io.acrosafe.wallet.eth.config.ApplicationProperties;
import io.acrosafe.wallet.eth.domain.EnterpriseAccountRecord;
import io.acrosafe.wallet.eth.exception.InvalidCoinSymbolException;
import io.acrosafe.wallet.eth.repository.EnterpriseAccountRecordRepository;

@Service
public class EnterpriseAccountService
{
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(EnterpriseAccountService.class);

    private static final String ETH_SYMBOL = "ETH";

    @Autowired
    private SeedGenerator seedGenerator;

    @Autowired
    private BlockChainNetwork blockChainNetwork;

    @Autowired
    private EnterpriseAccountCacheService enterpriseAccountCacheService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private EnterpriseAccountRecordRepository enterpriseAccountRecordRepository;

    /**
     * Creates new enterprise account based on given symbol and label.
     * 
     * @param label
     * @param symbol
     * @return
     * @throws CryptoException
     * @throws InvalidCredentialException
     * @throws InvalidCoinSymbolException
     */
    @Transactional
    public EnterpriseAccountRecord createEnterpriseAccount(String label, String symbol)
            throws CryptoException, InvalidCredentialException, InvalidCoinSymbolException
    {
        if (StringUtils.isEmpty(symbol) || !symbol.equalsIgnoreCase(ETH_SYMBOL))
        {
            throw new InvalidCoinSymbolException("coin symbol is not valid.");
        }

        final byte[] seed = this.seedGenerator.getSeed(this.applicationProperties.getServiceId(), 256, 256);

        final byte[] spec = CryptoUtils.generateIVParameterSpecBytes();
        final String encodedSpec = Base64.getEncoder().encodeToString(spec);
        final byte[] ownerSalt = CryptoUtils.generateSaltBytes();
        final String encodedOwnerSalt = Base64.getEncoder().encodeToString(ownerSalt);

        String encryptedSeed = null;
        try
        {
            encryptedSeed =
                    CryptoUtils.encrypt(this.applicationProperties.getPassphrase().getStringValue(), seed, spec, ownerSalt);
        }
        catch (Throwable t)
        {
            // this shouldn't happen at all.
            throw new CryptoException("Invalid crypto operation.", t);
        }

        final String id = IDGenerator.randomUUID().toString();
        ETHAccount account = new ETHAccount(encryptedSeed, spec, ownerSalt, this.applicationProperties.getTestnet(),
                this.applicationProperties.getPassphrase());

        EnterpriseAccountRecord enterpriseAccountRecord = new EnterpriseAccountRecord();
        enterpriseAccountRecord.setId(id);
        enterpriseAccountRecord.setLabel(label);
        enterpriseAccountRecord.setEnabled(true);
        enterpriseAccountRecord.setSeed(encryptedSeed);
        enterpriseAccountRecord.setSpec(encodedSpec);
        enterpriseAccountRecord.setSalt(encodedOwnerSalt);
        enterpriseAccountRecord.setAddress(account.getAddress());
        enterpriseAccountRecord.setCreatedDate(Instant.now());

        this.enterpriseAccountRecordRepository.save(enterpriseAccountRecord);

        this.enterpriseAccountCacheService.addEnterpriseAccountToCache(id, account);

        return enterpriseAccountRecord;
    }

    @Transactional
    public List<EnterpriseAccountRecord> getEnterpriseAccounts(int pageId, int size)
    {
        Pageable pageable = PageRequest.of(pageId, size, Sort.by(Sort.Direction.ASC, "CreatedDate"));
        return this.enterpriseAccountRecordRepository.findAllByEnabledTrue(pageable);
    }

    /**
     * Returns the enterprise account based on given id.
     * 
     * @param enterpriseAccountId
     * @return
     */
    public ETHAccount getEnterpriseAccount(String enterpriseAccountId) throws AccountNotFoundException
    {
        ETHAccount account = this.enterpriseAccountCacheService.getEnterpriseAccount(enterpriseAccountId);
        return account;
    }

    @Transactional
    public String getAccountAddress(String accountId) throws AccountNotFoundException
    {
        ETHAccount account = this.enterpriseAccountCacheService.getEnterpriseAccount(accountId);

        return account.getAddress();
    }

    /**
     * Returns the account balance.
     * 
     * @param accountId
     * @return
     * @throws AccountNotFoundException
     * @throws InvalidCredentialException
     * @throws CryptoException
     * @throws GetBalanceException
     */
    public String getBalance(String accountId) throws AccountNotFoundException, GetBalanceException
    {
        ETHAccount account = this.enterpriseAccountCacheService.getEnterpriseAccount(accountId);

        try
        {
            final String address = account.getAddress();
            logger.info("getting balance for address {}.", address);
            EthGetBalance ethGetBalance = this.blockChainNetwork.getETHBalance(address);

            BigInteger wei = ethGetBalance.getBalance();

            return wei.toString();
        }
        catch (Throwable e)
        {
            throw new GetBalanceException("failed to get balance through RPC call.", e.getCause());
        }
    }
}
