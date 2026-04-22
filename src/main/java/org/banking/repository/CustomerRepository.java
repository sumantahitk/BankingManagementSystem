package org.banking.repository;

import org.banking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByUser(org.banking.entity.User user);
    boolean existsByEmail(String email);
}
