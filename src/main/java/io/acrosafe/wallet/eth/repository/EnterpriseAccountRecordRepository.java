package io.acrosafe.wallet.eth.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.acrosafe.wallet.eth.domain.EnterpriseAccountRecord;

public interface EnterpriseAccountRecordRepository extends JpaRepository<EnterpriseAccountRecord, String>
{
    public List<EnterpriseAccountRecord> findAllByEnabledTrue(Pageable pageable);

    public List<EnterpriseAccountRecord> findAllByEnabledTrue();
}
