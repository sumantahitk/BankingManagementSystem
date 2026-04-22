package org.banking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.banking.dto.request.LoginRequest;
import org.banking.dto.request.RegisterRequest;
import org.banking.dto.response.AuthResponse;
import org.banking.entity.Customer;
import org.banking.entity.Role;
import org.banking.entity.User;
import org.banking.exception.DuplicateResourceException;
import org.banking.repository.CustomerRepository;
import org.banking.repository.UserRepository;
import org.banking.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new DuplicateResourceException("Username taken");
        if (userRepository.existsByEmail(request.getEmail()))
            throw new DuplicateResourceException("Email taken");

        Role finalRole = ("ADMIN".equalsIgnoreCase(request.getRole())) ? Role.ROLE_ADMIN : Role.ROLE_CUSTOMER;

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(finalRole)
                .build();
        User savedUser = userRepository.save(user);

        Customer customer = Customer.builder()
                .customerName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .user(savedUser)
                .build();
        customerRepository.save(customer);

        return login(new LoginRequest(request.getUsername(), request.getPassword()));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String token = tokenProvider.generateToken(auth);
        User user = (User) auth.getPrincipal();

        Customer customer = customerRepository.findByUser(user).orElse(null);
        String fullName = (customer != null) ? customer.getCustomerName() : user.getUsername();
        java.util.UUID customerId = (customer != null) ? customer.getId() : null;

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .customerId(customerId)
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(fullName)
                .role(user.getRole().name())
                .build();
    }
}
