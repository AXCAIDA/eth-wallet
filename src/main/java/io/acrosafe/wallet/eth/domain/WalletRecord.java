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
package io.acrosafe.wallet.eth.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "wallet_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WalletRecord implements Serializable
{
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    @Column(name = "wallet_address", nullable = true)
    private String address;

    @Column(name = "enterprise_account_id", nullable = false)
    private String enterpriseAccountId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "label", nullable = true)
    private String label;

    @Column(name = "signer_seed", nullable = false)
    private String signerSeed;

    @Column(name = "signer_address", nullable = false)
    private String signerAddress;

    @Column(name = "backup_seed", nullable = false)
    private String backupSeed;

    @Column(name = "backup_address", nullable = false)
    private String backupAddress;

    @Column(name = "spec", nullable = false)
    private String spec;

    @Column(name = "salt", nullable = false)
    private String salt;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public Instant getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getEnterpriseAccountId()
    {
        return enterpriseAccountId;
    }

    public void setEnterpriseAccountId(String enterpriseAccountId)
    {
        this.enterpriseAccountId = enterpriseAccountId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getSignerSeed()
    {
        return signerSeed;
    }

    public void setSignerSeed(String signerSeed)
    {
        this.signerSeed = signerSeed;
    }

    public String getSignerAddress()
    {
        return signerAddress;
    }

    public void setSignerAddress(String signerAddress)
    {
        this.signerAddress = signerAddress;
    }

    public String getBackupSeed()
    {
        return backupSeed;
    }

    public void setBackupSeed(String backupSeed)
    {
        this.backupSeed = backupSeed;
    }

    public String getBackupAddress()
    {
        return backupAddress;
    }

    public void setBackupAddress(String backupAddress)
    {
        this.backupAddress = backupAddress;
    }

    public String getSpec()
    {
        return spec;
    }

    public void setSpec(String spec)
    {
        this.spec = spec;
    }

    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }
}
