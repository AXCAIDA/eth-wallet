package io.acrosafe.wallet.eth.repository;

import java.util.List;
import java.util.Optional;

import io.acrosafe.wallet.core.eth.Token;
import io.acrosafe.wallet.eth.domain.TransactionRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long>
{
    Optional<TransactionRecord> findFirstByTransactionId(String transactionId);

    List<TransactionRecord> findAllByWalletIdAndToken(Pageable pageable, String walletId, Token token);
}
