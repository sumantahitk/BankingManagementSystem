package org.banking.repository;

import org.banking.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByAccountAccountNumber(Long accountNumber, Pageable pageable);
    Page<Transaction> findByAccountAccountNumberAndType(Long accountNumber, org.banking.entity.TransactionType type, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type AND t.status = 'SUCCESS'")
    java.math.BigDecimal sumAmountByType(org.banking.entity.TransactionType type);

    @org.springframework.data.jpa.repository.Query("SELECT t.account.customer.customerName, SUM(t.amount), COUNT(t) FROM Transaction t WHERE t.status = 'SUCCESS' GROUP BY t.account.customer.customerName ORDER BY SUM(t.amount) DESC")
    java.util.List<Object[]> findTopUsersByVolume(org.springframework.data.domain.Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT CAST(t.timestamp AS date), SUM(t.amount) FROM Transaction t WHERE t.status = 'SUCCESS' GROUP BY CAST(t.timestamp AS date) ORDER BY CAST(t.timestamp AS date) DESC")
    java.util.List<Object[]> findDailyVolumes();

    long countByStatus(org.banking.entity.TransactionStatus status);

    @org.springframework.data.jpa.repository.Query("SELECT t FROM Transaction t WHERE t.timestamp >= :startOfDay AND (:type IS NULL OR t.type = :type)")
    Page<Transaction> findTransactionsToday(java.time.LocalDateTime startOfDay, org.banking.entity.TransactionType type, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT t.account.customer FROM Transaction t WHERE t.timestamp >= :startOfMonth")
    Page<org.banking.entity.Customer> findActiveCustomersSince(java.time.LocalDateTime startOfMonth, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT t FROM Transaction t WHERE t.id = :id")
    java.util.Optional<Transaction> findByIdDetailed(Long id);
}
