package com.example.multiwallet.service;

import com.example.multiwallet.dto.auth.*;
import com.example.multiwallet.dto.otp.SendOtpRequest;
import com.example.multiwallet.dto.otp.VerifyOtpRequest;
import com.example.multiwallet.dto.user.UserResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void sendRegistrationOtp(SendOtpRequest request);

    UserResponse verifyRegistrationOtp(VerifyOtpRequest request);

    boolean verifyOtp(VerifyOtpRequest request);

    void resendOtp(ResendOtpRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}