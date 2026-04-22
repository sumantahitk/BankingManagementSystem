package org.banking.controller;

import lombok.RequiredArgsConstructor;
import org.banking.dto.response.*;
import org.banking.entity.TransactionType;
import org.banking.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success("Admin dashboard statistics", adminService.getDashboardStats()));
    }

    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<PagedResponse<CustomerResponse>>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("All customers retrieved", adminService.getAllCustomers(page, size)));
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Customer found", adminService.getCustomerById(id)));
    }

    @GetMapping("/customers/active")
    public ResponseEntity<ApiResponse<PagedResponse<CustomerResponse>>> getActiveCustomers(
            @RequestParam(defaultValue = "30") int days,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Active customers in last " + days + " days", adminService.getActiveCustomersLastDays(days, page, size)));
    }

    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("All transactions retrieved", adminService.getAllTransactions(page, size)));
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Transaction found", adminService.getTransactionById(id)));
    }

    @GetMapping("/transactions/today")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getTransactionsToday(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Today's transactions retrieved", adminService.getTransactionsToday(type, page, size)));
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable UUID id) {
        adminService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer deleted by admin", null));
    }
}
