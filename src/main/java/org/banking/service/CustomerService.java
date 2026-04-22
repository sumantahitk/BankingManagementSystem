package org.banking.service;

import org.banking.dto.response.CustomerResponse;
import org.banking.dto.response.PagedResponse;
import java.util.UUID;

public interface CustomerService {
    CustomerResponse getCustomer(UUID id);
    CustomerResponse getCustomerByEmail(String email);
    PagedResponse<CustomerResponse> getAllCustomers(int page, int size);
    CustomerResponse updateCustomer(UUID id, org.banking.dto.request.CustomerUpdateRequest request);
    void deleteCustomer(UUID id);
}
