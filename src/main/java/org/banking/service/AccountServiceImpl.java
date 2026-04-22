package org.banking.service;

import lombok.RequiredArgsConstructor;
import org.banking.dto.request.*;
import org.banking.dto.response.AccountResponse;
import org.banking.dto.response.TransactionResponse;
import org.banking.entity.*;
import org.banking.exception.*;
import org.banking.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AccountResponse createAccount(AccountCreateRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        
        Account account = Account.builder()
                .balance(request.getInitialBalance())
                .accountType(request.getAccountType())
                .customer(customer)
                .transactionPin(passwordEncoder.encode(request.getPin()))
                .status(AccountStatus.ACTIVE)
                .build();
        
        return mapToResponse(accountRepository.save(account));
    }

    @Override @Transactional(readOnly = true) public AccountResponse getAccount(Long id) { return mapToResponse(findAccount(id)); }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getCustomerAccounts(String email) {
        Customer c = customerRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return c.getAccounts().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransactionResponse deposit(Long id, DepositRequest request) {
        Account a = accountRepository.findByIdWithLock(id).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        if (a.getStatus() != AccountStatus.ACTIVE) throw new InvalidOperationException("Account not active");
        a.setBalance(a.getBalance().add(request.getAmount()));
        accountRepository.save(a);
        return mapToTransactionResponse(createTransaction(a, TransactionType.DEPOSIT, request.getAmount(), request.getDescription(), "SYSTEM", a.getCustomer().getCustomerName()));
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(Long id, WithdrawRequest request) {
        Account a = accountRepository.findByIdWithLock(id).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        
        verifyPin(a, request.getPin());
        
        if (a.getStatus() != AccountStatus.ACTIVE) throw new InvalidOperationException("Account not active");
        if (a.getBalance().compareTo(request.getAmount()) < 0) throw new InsufficientBalanceException("Insufficient funds");
        
        a.setBalance(a.getBalance().subtract(request.getAmount()));
        accountRepository.save(a);
        return mapToTransactionResponse(createTransaction(a, TransactionType.WITHDRAWAL, request.getAmount(), request.getDescription(), a.getCustomer().getCustomerName(), "CASH/ATM"));
    }

    @Override
    @Transactional
    public TransactionResponse transfer(Long fromId, TransferRequest request) {
        if (fromId.equals(request.getToAccountNumber())) throw new InvalidOperationException("Cannot transfer to self");
        
        Long first = Math.min(fromId, request.getToAccountNumber());
        Long second = Math.max(fromId, request.getToAccountNumber());
        
        Account a1 = accountRepository.findByIdWithLock(first).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        Account a2 = accountRepository.findByIdWithLock(second).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        
        Account from = (fromId.equals(a1.getAccountNumber())) ? a1 : a2;
        Account to = (fromId.equals(a1.getAccountNumber())) ? a2 : a1;
        
        verifyPin(from, request.getPin());
        
        if (from.getStatus() != AccountStatus.ACTIVE) throw new InvalidOperationException("Sender account not active");
        if (from.getBalance().compareTo(request.getAmount()) < 0) throw new InsufficientBalanceException("Insufficient funds");
        
        from.setBalance(from.getBalance().subtract(request.getAmount()));
        to.setBalance(to.getBalance().add(request.getAmount()));
        
        accountRepository.save(from); 
        accountRepository.save(to);
        
        return mapToTransactionResponse(createTransaction(from, TransactionType.TRANSFER, request.getAmount(), "Transfer to " + to.getAccountNumber(), from.getCustomer().getCustomerName(), to.getCustomer().getCustomerName()));
    }

    @Override public void deleteAccount(Long id) { accountRepository.deleteById(id); }

    private void verifyPin(Account account, String rawPin) {
        if (!passwordEncoder.matches(rawPin, account.getTransactionPin())) {
            throw new InvalidPinException("Invalid transaction PIN");
        }
    }

    private Account findAccount(Long id) { return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found")); }
    private AccountResponse mapToResponse(Account a) { return AccountResponse.builder().accountNumber(a.getAccountNumber()).balance(a.getBalance()).accountType(a.getAccountType()).status(a.getStatus()).build(); }
    private TransactionResponse mapToTransactionResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .type(t.getType())
                .amount(t.getAmount())
                .timestamp(t.getTimestamp())
                .status(t.getStatus())
                .description(t.getDescription())
                .referenceNumber(t.getReferenceNumber())
                .accountNumber(t.getAccount().getAccountNumber())
                .customerId(t.getAccount().getCustomer().getId())
                .senderName(t.getSenderName())
                .receiverName(t.getReceiverName())
                .build();
    }

    private Transaction createTransaction(Account a, TransactionType type, BigDecimal amount, String desc, String sender, String receiver) {
        Transaction t = Transaction.builder()
                .account(a)
                .type(type)
                .amount(amount)
                .description(desc)
                .senderName(sender)
                .receiverName(receiver)
                .status(TransactionStatus.SUCCESS)
                .build();
        return transactionRepository.save(t);
    }
}
