package io.acrosafe.wallet.eth.service;

import io.acrosafe.wallet.core.eth.ETHAccount;
import io.acrosafe.wallet.core.eth.exception.AccountNotFoundException;
import io.acrosafe.wallet.eth.config.ApplicationProperties;
import io.acrosafe.wallet.eth.domain.EnterpriseAccountRecord;
import io.acrosafe.wallet.eth.repository.EnterpriseAccountRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
            }

            logger.info("restore {} enterprise accounts.", enterpriseAccountRecords.size());
        }
    }
}
