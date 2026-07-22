package com.example.multiwallet.service;

import com.example.multiwallet.dto.auth.LoginRequest;
import com.example.multiwallet.dto.auth.LoginResponse;
import com.example.multiwallet.dto.otp.SendOtpRequest;
import com.example.multiwallet.dto.otp.VerifyOtpRequest;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void sendRegistrationOtp(SendOtpRequest request);

    boolean verifyOtp(VerifyOtpRequest request);

}