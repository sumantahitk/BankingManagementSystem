package org.banking.controller;

import lombok.RequiredArgsConstructor;
import org.banking.dto.response.ApiResponse;
import org.banking.dto.response.PagedResponse;
import org.banking.dto.response.TransactionResponse;
import org.banking.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Transactions list", transactionService.getAllTransactions(page, size)));
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getAccountTransactions(
            @PathVariable Long accountNumber,
            @RequestParam(required = false) org.banking.entity.TransactionType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Account transactions", transactionService.getAccountTransactions(accountNumber, type, page, size)));
    }
}
