package org.banking.service;

import org.banking.dto.response.PagedResponse;
import org.banking.dto.response.TransactionResponse;

public interface TransactionService {
    PagedResponse<TransactionResponse> getAccountTransactions(Long accountNumber, org.banking.entity.TransactionType type, int page, int size);
    PagedResponse<TransactionResponse> getAllTransactions(int page, int size);
}
