package com.agriserve.service;

import com.agriserve.dto.auth.AuthResponse;
import com.agriserve.dto.auth.LoginRequest;
import com.agriserve.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
