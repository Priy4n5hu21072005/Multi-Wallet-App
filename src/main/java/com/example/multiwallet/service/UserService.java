package com.example.multiwallet.service;
import com.example.multiwallet.dto.user.RegisterUserRequest;
import com.example.multiwallet.dto.user.UpdateUserRequest;
import com.example.multiwallet.dto.user.UserResponse;

import java.util.UUID;
import java.util.List;
public interface UserService {
    void registerUser(RegisterUserRequest request);
    UserResponse getUserById(UUID id);
    UserResponse getUserByEmail(String email);
    List<UserResponse> getAllUsers();
    void deleteUser(UUID id);
    UserResponse updateUser(UUID id, UpdateUserRequest request);
}