package org.banking.service;

import org.banking.dto.request.DepositRequest;
import org.banking.dto.response.TransactionResponse;
import org.banking.entity.Account;
import org.banking.entity.AccountStatus;
import org.banking.entity.Transaction;
import org.banking.exception.InvalidOperationException;
import org.banking.exception.ResourceNotFoundException;
import org.banking.repository.AccountRepository;
import org.banking.repository.CustomerRepository;
import org.banking.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock private AccountRepository accountRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private TransactionRepository transactionRepository;
    @InjectMocks private AccountServiceImpl accountService;
    private Account account;

    @BeforeEach void setUp() {
        account = Account.builder().accountNumber(12345L).balance(new BigDecimal("1000.00")).status(AccountStatus.ACTIVE).build();
    }

    @Test void deposit_Success() {
        DepositRequest request = new DepositRequest(new BigDecimal("500.00"), "Test");
        when(accountRepository.findByIdWithLock(12345L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
        TransactionResponse response = accountService.deposit(12345L, request);
        assertEquals(new BigDecimal("1500.00"), account.getBalance());
        assertNotNull(response);
    }
}
