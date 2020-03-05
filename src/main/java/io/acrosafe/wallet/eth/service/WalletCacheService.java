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

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.acrosafe.wallet.core.eth.ETHAccount;
import io.acrosafe.wallet.core.eth.ETHWallet;
import io.acrosafe.wallet.core.eth.Token;
import io.acrosafe.wallet.core.eth.exception.AccountNotFoundException;
import io.acrosafe.wallet.core.eth.exception.TokenNotSupportedException;
import io.acrosafe.wallet.core.eth.exception.WalletNotFoundException;
import io.acrosafe.wallet.eth.config.ApplicationProperties;
import io.acrosafe.wallet.eth.domain.TokenRecord;
import io.acrosafe.wallet.eth.domain.WalletRecord;
import io.acrosafe.wallet.eth.repository.TokenRecordRepository;
import io.acrosafe.wallet.eth.repository.WalletRecordRepository;

@Service
public class WalletCacheService
{
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(EnterpriseAccountCacheService.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private WalletRecordRepository walletRecordRepository;

    @Autowired
    private TokenRecordRepository tokenRecordRepository;

    @Autowired
    private EnterpriseAccountCacheService enterpriseAccountCacheService;

    private Map<String, ETHWallet> wallets = new ConcurrentHashMap<>();
    private Map<String, Token> tokens = new HashMap<>();

    @PostConstruct
    public void initialize()
    {
        try
        {
            restoreTokens();
            restoreWallets();
        }
        catch (Throwable t)
        {
            logger.error("failed to load enterprise accounts.", t);
        }
    }

    public void addWalletToCache(String walletId, ETHWallet wallet)
    {
        wallets.put(walletId, wallet);
    }

    public ETHWallet getWallet(String walletId) throws WalletNotFoundException
    {
        ETHWallet wallet = this.wallets.get(walletId);
        if (wallet == null)
        {
            throw new WalletNotFoundException("failed to find wallet " + walletId);
        }

        return wallet;
    }

    public Token getToken(String symbol) throws TokenNotSupportedException
    {
        Token token = this.tokens.get(symbol);
        if (token == null)
        {
            throw new TokenNotSupportedException("failed to find token " + symbol);
        }

        return token;
    }

    public List<Token> getTokens()
    {
        return new ArrayList<>(tokens.values());
    }

    public List<ETHWallet> getWallets()
    {
        return new ArrayList<>(wallets.values());
    }

    private void restoreTokens()
    {
        List<TokenRecord> tokenRecords = this.tokenRecordRepository.findAll();
        if (tokenRecords != null && tokenRecords.size() != 0)
        {
            for (TokenRecord tokenRecord : tokenRecords)
            {
                Token token = new Token();
                final String symbol = tokenRecord.getSymbol();
                final String contractAddress = tokenRecord.getContractAddress();
                token.setContractAddress(contractAddress);
                token.setSymbol(symbol);
                token.setDecimals(tokenRecord.getDecimals());
                token.setName(tokenRecord.getName());

                logger.info("added {} to token cache. contractAddress = {}, enabled = {}", symbol, contractAddress,
                        tokenRecord.isEnabled());
                tokens.put(symbol, token);
            }
        }

    }

    private void restoreWallets() throws AccountNotFoundException
    {
        List<WalletRecord> walletRecords = this.walletRecordRepository.findAllByEnabledTrue();
        for (WalletRecord walletRecord : walletRecords)
        {
            if (walletRecord.getAddress() != null)
            {
                ETHAccount signerAccount = new ETHAccount(walletRecord.getSignerSeed(),
                        Base64.getDecoder().decode(walletRecord.getSpec()), Base64.getDecoder().decode(walletRecord.getSalt()),
                        applicationProperties.getTestnet(), walletRecord.getSignerAddress());

                ETHAccount backupSignerAccount = new ETHAccount(walletRecord.getBackupSeed(),
                        Base64.getDecoder().decode(walletRecord.getSpec()), Base64.getDecoder().decode(walletRecord.getSalt()),
                        applicationProperties.getTestnet(), walletRecord.getBackupAddress());

                ETHAccount enterpriseAccount =
                        this.enterpriseAccountCacheService.getEnterpriseAccount(walletRecord.getEnterpriseAccountId());

                ETHWallet wallet = new ETHWallet(walletRecord.getId(), enterpriseAccount, signerAccount, backupSignerAccount);

                String contractAddress = walletRecord.getAddress();
                if (!StringUtils.isEmpty(contractAddress))
                {
                    wallet.setAddress(walletRecord.getAddress());
                    logger.info("adding wallet {} to cache. address = {}", walletRecord.getId(), walletRecord.getAddress());
                    this.wallets.put(walletRecord.getId(), wallet);
                }
            }
        }

        logger.info("restored {} wallet.", wallets.size());
    }
}
