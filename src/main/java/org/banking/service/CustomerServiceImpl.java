package org.banking.service;

import lombok.RequiredArgsConstructor;
import org.banking.dto.response.CustomerResponse;
import org.banking.dto.response.PagedResponse;
import org.banking.entity.Customer;
import org.banking.exception.ResourceNotFoundException;
import org.banking.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomer(UUID id) {
        return mapToResponse(customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByEmail(String email) {
        return mapToResponse(customerRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Customer not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<CustomerResponse> getAllCustomers(int page, int size) {
        Page<Customer> customers = customerRepository.findAll(PageRequest.of(page, size));
        return PagedResponse.<CustomerResponse>builder()
                .content(customers.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .page(customers.getNumber()).size(customers.getSize()).totalElements(customers.getTotalElements()).totalPages(customers.getTotalPages()).last(customers.isLast()).build();
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(UUID id, org.banking.dto.request.CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        
        if (request.getAddress() != null) customer.setAddress(request.getAddress());
        if (request.getDob() != null) customer.setDob(request.getDob());
        
        return mapToResponse(customerRepository.save(customer));
    }

    @Override public void deleteCustomer(UUID id) { customerRepository.deleteById(id); }

    private CustomerResponse mapToResponse(Customer c) { return CustomerResponse.builder().id(c.getId()).customerName(c.getCustomerName()).email(c.getEmail()).phone(c.getPhone()).address(c.getAddress()).build(); }
}
