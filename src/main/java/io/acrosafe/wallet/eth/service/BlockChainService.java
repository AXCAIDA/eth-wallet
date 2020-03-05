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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import io.acrosafe.wallet.core.eth.BlockChainNetwork;
import io.acrosafe.wallet.core.eth.ETHWallet;
import io.acrosafe.wallet.core.eth.IDGenerator;
import io.acrosafe.wallet.core.eth.Token;
import io.acrosafe.wallet.core.eth.TransactionStatus;
import io.acrosafe.wallet.core.eth.TransactionType;
import io.acrosafe.wallet.core.eth.TransactionUtils;
import io.acrosafe.wallet.core.eth.exception.ContractCreationException;
import io.acrosafe.wallet.eth.domain.AddressRecord;
import io.acrosafe.wallet.eth.domain.TransactionRecord;
import io.acrosafe.wallet.eth.domain.WalletRecord;
import io.acrosafe.wallet.eth.repository.AddressRecordRepository;
import io.acrosafe.wallet.eth.repository.TransactionRecordRepository;
import io.acrosafe.wallet.eth.repository.WalletRecordRepository;

@Service
public class BlockChainService
{
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(BlockChainService.class);

    @Autowired
    private BlockChainNetwork blockChainNetwork;

    @Autowired
    private WalletCacheService walletCacheService;

    @Autowired
    private WalletRecordRepository walletRecordRepository;

    @Autowired
    private AddressRecordRepository addressRecordRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    public EthGetBalance getEnterpriseAccountBalance(String address)
    {
        return blockChainNetwork.getETHBalance(address);
    }

    public synchronized Map<String, BigInteger> getBalances(String walletAddress, List<Token> tokens)
    {
        return blockChainNetwork.getBalance(walletAddress, tokens);
    }

    public synchronized void subscribe(String address, String walletId)
    {
        this.blockChainNetwork.getETHFilter(address).subscribe(log -> {
            final String hash = log.getTransactionHash();

            try
            {
                EthTransaction transaction = this.blockChainNetwork.getTransactionByHash(hash);
                EthGetTransactionReceipt receipt = this.blockChainNetwork.getTransactionReceiptByHash(hash);
                TransactionReceipt transactionReceipt = receipt.getTransactionReceipt().orElse(null);
                TransactionStatus status = TransactionUtils.getTransactionStatus(transactionReceipt);

                TransactionRecord existingTransactionRecord =
                        this.transactionRecordRepository.findFirstByTransactionId(hash).orElse(null);
                if (existingTransactionRecord == null)
                {
                    TransactionRecord transactionRecord = new TransactionRecord();
                    transactionRecord.setId(IDGenerator.randomUUID().toString());
                    transactionRecord.setStatus(status);
                    transactionRecord.setAmount(transaction.getResult().getValue());
                    transactionRecord.setCreatedDate(Instant.now());
                    transactionRecord.setFee(BigInteger.ZERO);
                    transactionRecord.setLastModifiedDate(Instant.now());
                    transactionRecord.setTransactionId(hash);
                    transactionRecord.setTransactionType(TransactionType.DEPOSIT);
                    transactionRecord.setWalletId(walletId);
                    transactionRecord.setToken("ETH");
                    transactionRecord.setDestination(address);

                    this.transactionRecordRepository.save(transactionRecord);
                    logger.info("found new deposite {} for address {}. value = {}, status = {}", hash, address,
                            transaction.getResult().getValue(), status);
                }
                else
                {
                    if (existingTransactionRecord.getStatus() != TransactionStatus.CONFIRMED)
                    {
                        existingTransactionRecord.setStatus(status);
                        this.transactionRecordRepository.save(existingTransactionRecord);
                        logger.info("updated existing transaction status. hash = {}, address = {}, status = {}", hash, address,
                                status);
                    }
                    else
                    {
                        logger.info("found existing deposit record. hash = {}, address = {}, status = {}, value = {}", hash,
                                address, status, existingTransactionRecord.getAmount());
                    }
                }
            }
            catch (Throwable t)
            {
                // we have to let it go
                logger.warn("failed to add listener to address {}", address, t);
            }

        });

    }

    @Async
    public synchronized void deployAddressContract(String addressId, Credentials credentials, String ownerAccountAddress)
            throws ContractCreationException
    {
        try
        {
            String contractAddress = this.blockChainNetwork.deployAddressContract(ownerAccountAddress, credentials,
                    BigInteger.valueOf(12_000_000_000L), BigInteger.valueOf(2300000));
            if (!StringUtils.isEmpty(contractAddress))
            {
                AddressRecord record = this.addressRecordRepository.findById(addressId).orElse(null);
                if (record != null)
                {
                    record.setAddress(contractAddress);
                    this.addressRecordRepository.save(record);

                    subscribe(contractAddress, record.getWalletId());

                    logger.info(
                            "address {} has been deployed to blockchain and persisted into DB. contract address = {}, owner account address = {}",
                            addressId, contractAddress, ownerAccountAddress);
                }
                else
                {
                    // this is almost impossible
                    logger.warn("failed to find address {} in DB.", addressId);
                }
            }
            else
            {
                throw new ContractCreationException("address contract is not valid.");
            }
        }
        catch (Throwable t)
        {
            throw new ContractCreationException("failed to deploy address sub-contract on blockchain.");
        }

    }

    @Async
    public synchronized void deployWalletContract(ETHWallet multisigWallet, String walletId, Credentials credentials,
            List<String> signingKeys) throws ContractCreationException
    {
        try
        {
            String contractAddress = this.blockChainNetwork.deployWalletContract(credentials, signingKeys,
                    BigInteger.valueOf(12_000_000_000L), BigInteger.valueOf(2300000));

            if (!StringUtils.isEmpty(contractAddress))
            {
                WalletRecord record = walletRecordRepository.findById(walletId).orElse(null);
                if (record != null)
                {
                    record.setAddress(contractAddress);
                    this.walletRecordRepository.save(record);

                    multisigWallet.setReady(true);
                    multisigWallet.setAddress(contractAddress);
                    this.walletCacheService.addWalletToCache(walletId, multisigWallet);
                    logger.info("wallet {} has been deployed to blockchain and persisted into DB. contract address = {}",
                            walletId, contractAddress);
                }
                else
                {
                    // this is almost impossible
                    logger.warn("failed to find wallet {} in DB.", walletId);
                }
            }
            else
            {
                // this is almost impossible
                logger.warn("failed to find wallet {} in DB.", walletId);
            }
        }
        catch (Throwable t)
        {
            throw new ContractCreationException("failed to deploy wallet contract on blockchain.", t);
        }

    }
}
