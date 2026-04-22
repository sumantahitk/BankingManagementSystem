package org.banking.controller;

import lombok.RequiredArgsConstructor;
import org.banking.dto.response.ApiResponse;
import org.banking.dto.response.CustomerResponse;
import org.banking.dto.response.PagedResponse;
import org.banking.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<CustomerResponse>>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Customers list", customerService.getAllCustomers(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Customer found", customerService.getCustomer(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable UUID id, @jakarta.validation.Valid @RequestBody org.banking.dto.request.CustomerUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Profile updated", customerService.updateCustomer(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer deleted", null));
    }
}
