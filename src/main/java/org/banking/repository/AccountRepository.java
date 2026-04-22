package org.banking.repository;

import org.banking.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber")
    Optional<Account> findByIdWithLock(@Param("accountNumber") Long accountNumber);

    @Query("SELECT SUM(a.balance) FROM Account a")
    java.math.BigDecimal sumAllBalances();

    @Query("SELECT a FROM Account a ORDER BY a.balance DESC")
    java.util.List<Account> findTopByBalance(org.springframework.data.domain.Pageable pageable);
}
