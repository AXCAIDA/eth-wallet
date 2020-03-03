package io.acrosafe.wallet.eth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.acrosafe.wallet.core.eth.BlockChainNetwork;
import io.acrosafe.wallet.core.eth.SeedGenerator;

@Configuration
public class BlockChainConfiguration
{
    private static final Long DEFAULT_SERVICE_TIMEOUT = 600L;

    private final Environment env;

    public BlockChainConfiguration(Environment env)
    {
        this.env = env;
    }

    @Bean
    public SeedGenerator seedGenerator()
    {
        return new SeedGenerator();
    }

    @Bean
    public BlockChainNetwork blockChainNetwork()
    {
        String serviceUrl = env.getProperty("application.service-url");
        Long serviceTimeout = env.getProperty("application.service-timeout", Long.class, DEFAULT_SERVICE_TIMEOUT);
        BlockChainNetwork blockChainNetwork = new BlockChainNetwork(serviceUrl, serviceTimeout);
        return blockChainNetwork;
    }
}
