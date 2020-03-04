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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import io.acrosafe.wallet.core.eth.BlockChainNetwork;
import io.acrosafe.wallet.core.eth.ETHWallet;
import io.acrosafe.wallet.core.eth.exception.ContractCreationException;
import io.acrosafe.wallet.eth.contract.Wallet;
import io.acrosafe.wallet.eth.domain.WalletRecord;
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

    public EthGetBalance getEnterpriseAccountBalance(String address)
    {
        return blockChainNetwork.getETHBalance(address);
    }

    @Async
    public synchronized void deployWalletContract(ETHWallet multisigWallet, String walletId, Credentials credentials,
            List<String> signingKeys) throws ContractCreationException
    {
        ContractGasProvider contractGasProvider =
                new StaticGasProvider(BigInteger.valueOf(12_000_000_000L), BigInteger.valueOf(2300000));

        try
        {
            Wallet contract =
                    Wallet.deploy(this.blockChainNetwork.getWeb3j(), credentials, contractGasProvider, signingKeys).send();
            if (contract.isValid())
            {
                WalletRecord record = walletRecordRepository.findById(walletId).orElse(null);
                if (record != null)
                {
                    final String contractAddress = contract.getContractAddress();
                    record.setAddress(contractAddress);
                    this.walletRecordRepository.save(record);

                    this.walletCacheService.addWalletToCache(walletId, multisigWallet);
                    this.walletCacheService.updateWalletStatus(walletId, true);
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
                throw new ContractCreationException("wallet contract is not valid.");
            }
        }
        catch (Throwable t)
        {
            throw new ContractCreationException("failed to deploy wallet contract on blockchain.", t);
        }

    }
}
