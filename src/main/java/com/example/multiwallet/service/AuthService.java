package com.example.multiwallet.service;

import com.example.multiwallet.dto.auth.LoginRequest;
import com.example.multiwallet.dto.auth.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}