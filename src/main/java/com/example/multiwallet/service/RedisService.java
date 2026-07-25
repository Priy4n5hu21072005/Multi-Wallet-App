package com.example.multiwallet.service;

import com.example.multiwallet.dto.user.RegisterUserRequest;

public interface RedisService {
    void savePendingUsers(RegisterUserRequest request , String otp);
    RegisterUserRequest getPendingUsers(String email);
    String getOtp(String email);
    void deletePendingUser(String email);
}
