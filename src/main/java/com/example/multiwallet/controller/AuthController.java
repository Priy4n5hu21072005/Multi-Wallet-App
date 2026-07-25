package com.example.multiwallet.controller;

import com.example.multiwallet.dto.auth.*;
import com.example.multiwallet.dto.otp.SendOtpRequest;
import com.example.multiwallet.dto.otp.VerifyOtpRequest;
import com.example.multiwallet.dto.user.RegisterUserRequest;
import com.example.multiwallet.dto.user.UserResponse;
import com.example.multiwallet.service.AuthService;
import com.example.multiwallet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("Registration OTP has been sent to your email (" + request.getEmail() + ")");
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        authService.sendRegistrationOtp(request);
        return ResponseEntity.ok("OTP sent successfully to " + request.getEmail());
    }

    @PostMapping("/verify-registration-otp")
    public ResponseEntity<UserResponse> verifyRegistrationOtp(@Valid @RequestBody VerifyOtpRequest request) {
        UserResponse response = authService.verifyRegistrationOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        authService.resendOtp(request);
        return ResponseEntity.ok("New OTP sent successfully to " + request.getEmail());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok("Password reset OTP has been sent to your email (" + request.getEmail() + ")");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Password reset successfully. You can now login with your new password.");
    }
}
