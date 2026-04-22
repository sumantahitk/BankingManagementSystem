package org.banking.service;

import org.banking.dto.request.LoginRequest;
import org.banking.dto.request.RegisterRequest;
import org.banking.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
