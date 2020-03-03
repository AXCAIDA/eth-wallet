package io.acrosafe.wallet.eth.config;

import io.acrosafe.wallet.core.eth.Passphrase;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private String serviceId;
    private Passphrase passphrase;
    private Boolean testnet = true;
    private String serviceUrl;
    private Long serviceTimeout = 600L;
    private int depositConfirmationNumber = 6;

    public Long getServiceTimeout() {
        return serviceTimeout;
    }

    public void setServiceTimeout(Long serviceTimeout) {
        this.serviceTimeout = serviceTimeout;
    }

    public Boolean getTestnet() {
        return testnet;
    }

    public void setTestnet(Boolean testnet) {
        this.testnet = testnet;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public int getDepositConfirmationNumber() {
        return depositConfirmationNumber;
    }

    public void setDepositConfirmationNumber(int depositConfirmationNumber) {
        this.depositConfirmationNumber = depositConfirmationNumber;
    }

    public Passphrase getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = new Passphrase(passphrase);
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
