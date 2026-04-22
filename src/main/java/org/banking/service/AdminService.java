package org.banking.service;

import org.banking.dto.response.AdminDashboardResponse;
import org.banking.dto.response.CustomerResponse;
import org.banking.dto.response.PagedResponse;
import org.banking.dto.response.TransactionResponse;
import java.util.UUID;

public interface AdminService {
    AdminDashboardResponse getDashboardStats();
    PagedResponse<CustomerResponse> getAllCustomers(int page, int size);
    CustomerResponse getCustomerById(UUID id); // New: Specific search
    PagedResponse<TransactionResponse> getAllTransactions(int page, int size);
    TransactionResponse getTransactionById(Long id); // New: Specific search
    PagedResponse<TransactionResponse> getTransactionsToday(org.banking.entity.TransactionType type, int page, int size); // New: Today's filter
    PagedResponse<CustomerResponse> getActiveCustomersLastDays(int days, int page, int size); // New: Flexible timeframe
    void deleteCustomer(UUID id);
}
