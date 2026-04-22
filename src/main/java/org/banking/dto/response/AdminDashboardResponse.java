package org.banking.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AdminDashboardResponse {
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private BigDecimal totalTransfers;
    private BigDecimal totalBankBalance; // New: Total money in the bank
    private long totalCustomers;
    private long totalAccounts;      // New: Total bank accounts
    private long totalTransactions;
    private long failedTransactions; // New: To calculate success rate
    
    private List<TopUserStats> topUsers;
    private List<TopAccountStats> topAccounts; // New: Richest accounts
    private List<DailyVolumeStats> dailyVolumes;
}

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TopAccountStats {
    private Long accountNumber;
    private String customerName;
    private BigDecimal balance;
}

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TopUserStats {
    private String customerName;
    private BigDecimal totalVolume;
    private long transactionCount;
}

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DailyVolumeStats {
    private String date;
    private BigDecimal volume;
}
