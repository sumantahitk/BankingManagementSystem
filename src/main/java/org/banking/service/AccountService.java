package org.banking.service;

import org.banking.dto.request.AccountCreateRequest;
import org.banking.dto.request.DepositRequest;
import org.banking.dto.request.TransferRequest;
import org.banking.dto.request.WithdrawRequest;
import org.banking.dto.response.AccountResponse;
import org.banking.dto.response.TransactionResponse;
import java.util.List;

public interface AccountService {
    AccountResponse createAccount(AccountCreateRequest request);
    AccountResponse getAccount(Long accountNumber);
    List<AccountResponse> getCustomerAccounts(String email);
    TransactionResponse deposit(Long accountNumber, DepositRequest request);
    TransactionResponse withdraw(Long accountNumber, WithdrawRequest request);
    TransactionResponse transfer(Long fromAccountNumber, TransferRequest request);
    void deleteAccount(Long accountNumber);
}
