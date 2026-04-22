package org.banking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.banking.dto.request.*;
import org.banking.dto.response.AccountResponse;
import org.banking.dto.response.ApiResponse;
import org.banking.dto.response.TransactionResponse;
import org.banking.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(201, "Account created", accountService.createAccount(request)));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable Long accountNumber) {
        return ResponseEntity.ok(ApiResponse.success("Account found", accountService.getAccount(accountNumber)));
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(@PathVariable Long accountNumber, @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Deposit successful", accountService.deposit(accountNumber, request)));
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(@PathVariable Long accountNumber, @Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Withdrawal successful", accountService.withdraw(accountNumber, request)));
    }

    @PostMapping("/{accountNumber}/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(@PathVariable Long accountNumber, @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Transfer successful", accountService.transfer(accountNumber, request)));
    }
}
