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

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.acrosafe.wallet.core.eth.ETHAccount;
import io.acrosafe.wallet.core.eth.exception.AccountNotFoundException;
import io.acrosafe.wallet.eth.config.ApplicationProperties;
import io.acrosafe.wallet.eth.domain.EnterpriseAccountRecord;
import io.acrosafe.wallet.eth.repository.EnterpriseAccountRecordRepository;

@Service
public class EnterpriseAccountCacheService
{
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(EnterpriseAccountCacheService.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private EnterpriseAccountRecordRepository enterpriseAccountRecordRepository;

    private Map<String, ETHAccount> enterpriseAccounts = new ConcurrentHashMap<>();

    @PostConstruct
    public void initialize()
    {
        try
        {
            restoreEnterpriseAccounts();
        }
        catch (Throwable t)
        {
            logger.error("failed to load enterprise accounts.", t);
        }
    }

    public void addEnterpriseAccountToCache(String enterpriseAccountId, ETHAccount enterpriseAccount)
    {
        enterpriseAccounts.put(enterpriseAccountId, enterpriseAccount);
    }

    public ETHAccount getEnterpriseAccount(String enterpriseAccountId) throws AccountNotFoundException
    {
        ETHAccount account = this.enterpriseAccounts.get(enterpriseAccountId);
        if (account == null)
        {
            throw new AccountNotFoundException("failed to find enterprise account " + enterpriseAccountId);
        }

        return account;
    }

    private void restoreEnterpriseAccounts()
    {
        List<EnterpriseAccountRecord> enterpriseAccountRecords = this.enterpriseAccountRecordRepository.findAllByEnabledTrue();

        if (enterpriseAccountRecords != null && enterpriseAccountRecords.size() != 0)
        {
            for (EnterpriseAccountRecord enterpriseAccountRecord : enterpriseAccountRecords)
            {
                final String encryptedSeed = enterpriseAccountRecord.getSeed();
                final byte[] spec = Base64.getDecoder().decode(enterpriseAccountRecord.getSpec());
                final byte[] salt = Base64.getDecoder().decode(enterpriseAccountRecord.getSalt());
                final String address = enterpriseAccountRecord.getAddress();
                ETHAccount account = new ETHAccount(encryptedSeed, spec, salt, applicationProperties.getTestnet(), address);

                this.enterpriseAccounts.put(enterpriseAccountRecord.getId(), account);
                logger.info("restored enterprise account {}.", enterpriseAccountRecord.getId());
            }

            logger.info("restore {} enterprise accounts.", enterpriseAccountRecords.size());
        }
    }
}
