package org.banking.service;

import lombok.RequiredArgsConstructor;
import org.banking.dto.response.*;
import org.banking.entity.Customer;
import org.banking.entity.Transaction;
import org.banking.entity.TransactionStatus;
import org.banking.entity.TransactionType;
import org.banking.exception.ResourceNotFoundException;
import org.banking.repository.AccountRepository;
import org.banking.repository.CustomerRepository;
import org.banking.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final TransactionService transactionService;

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboardStats() {
        BigDecimal totalDeposits = transactionRepository.sumAmountByType(TransactionType.DEPOSIT);
        BigDecimal totalWithdrawals = transactionRepository.sumAmountByType(TransactionType.WITHDRAWAL);
        BigDecimal totalTransfers = transactionRepository.sumAmountByType(TransactionType.TRANSFER);
        BigDecimal totalBankBalance = accountRepository.sumAllBalances();

        List<Object[]> topUsersData = transactionRepository.findTopUsersByVolume(PageRequest.of(0, 5));
        List<TopUserStats> topUsers = topUsersData.stream()
                .map(row -> new TopUserStats((String) row[0], (BigDecimal) row[1], (long) row[2]))
                .collect(Collectors.toList());

        List<TopAccountStats> topAccounts = accountRepository.findTopByBalance(PageRequest.of(0, 5)).stream()
                .map(acc -> new TopAccountStats(acc.getAccountNumber(), acc.getCustomer().getCustomerName(), acc.getBalance()))
                .collect(Collectors.toList());

        List<Object[]> dailyData = transactionRepository.findDailyVolumes();
        List<DailyVolumeStats> dailyVolumes = dailyData.stream()
                .map(row -> new DailyVolumeStats(row[0].toString(), (BigDecimal) row[1]))
                .collect(Collectors.toList());

        return AdminDashboardResponse.builder()
                .totalDeposits(totalDeposits != null ? totalDeposits : BigDecimal.ZERO)
                .totalWithdrawals(totalWithdrawals != null ? totalWithdrawals : BigDecimal.ZERO)
                .totalTransfers(totalTransfers != null ? totalTransfers : BigDecimal.ZERO)
                .totalBankBalance(totalBankBalance != null ? totalBankBalance : BigDecimal.ZERO)
                .totalCustomers(customerRepository.count())
                .totalAccounts(accountRepository.count())
                .totalTransactions(transactionRepository.count())
                .failedTransactions(transactionRepository.countByStatus(TransactionStatus.FAILED))
                .topUsers(topUsers)
                .topAccounts(topAccounts)
                .dailyVolumes(dailyVolumes)
                .build();
    }

    @Override
    public PagedResponse<CustomerResponse> getAllCustomers(int page, int size) {
        return customerService.getAllCustomers(page, size);
    }

    @Override
    public CustomerResponse getCustomerById(UUID id) {
        return customerService.getCustomer(id);
    }

    @Override
    public PagedResponse<TransactionResponse> getAllTransactions(int page, int size) {
        return transactionService.getAllTransactions(page, size);
    }

    @Override
    public TransactionResponse getTransactionById(Long id) {
        Transaction t = transactionRepository.findByIdDetailed(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return mapToTransactionResponse(t);
    }

    @Override
    public PagedResponse<TransactionResponse> getTransactionsToday(TransactionType type, int page, int size) {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        Page<Transaction> ts = transactionRepository.findTransactionsToday(startOfDay, type, PageRequest.of(page, size));
        return PagedResponse.<TransactionResponse>builder()
                .content(ts.getContent().stream().map(this::mapToTransactionResponse).collect(Collectors.toList()))
                .page(ts.getNumber()).size(ts.getSize()).totalElements(ts.getTotalElements()).totalPages(ts.getTotalPages()).last(ts.isLast()).build();
    }

    @Override
    public PagedResponse<CustomerResponse> getActiveCustomersLastDays(int days, int page, int size) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(days).with(LocalTime.MIN);
        Page<Customer> cs = transactionRepository.findActiveCustomersSince(startTime, PageRequest.of(page, size));
        return PagedResponse.<CustomerResponse>builder()
                .content(cs.getContent().stream().map(this::mapToCustomerResponse).collect(Collectors.toList()))
                .page(cs.getNumber()).size(cs.getSize()).totalElements(cs.getTotalElements()).totalPages(cs.getTotalPages()).last(cs.isLast()).build();
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID id) {
        customerService.deleteCustomer(id);
    }

    private TransactionResponse mapToTransactionResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId()).type(t.getType()).amount(t.getAmount()).timestamp(t.getTimestamp()).status(t.getStatus()).description(t.getDescription()).referenceNumber(t.getReferenceNumber())
                .accountNumber(t.getAccount().getAccountNumber()).customerId(t.getAccount().getCustomer().getId()).senderName(t.getSenderName()).receiverName(t.getReceiverName()).build();
    }

    private CustomerResponse mapToCustomerResponse(Customer c) {
        return CustomerResponse.builder().id(c.getId()).customerName(c.getCustomerName()).email(c.getEmail()).phone(c.getPhone()).address(c.getAddress()).build();
    }
}
