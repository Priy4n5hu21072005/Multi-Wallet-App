package com.example.multiwallet.service;
import com.example.multiwallet.dto.user.RegisterUserRequest;
import com.example.multiwallet.dto.user.UpdateUserRequest;
import com.example.multiwallet.dto.user.UserResponse;
import com.example.multiwallet.entity.User;
import java.util.UUID;
import java.util.List;
public interface UserService {
    UserResponse registerUser(RegisterUserRequest request);
    UserResponse getUserById(UUID id);
    List<UserResponse> getAllUsers();
    void deleteUser(UUID id);
    UserResponse updateUser(UUID id , UpdateUserRequest request);
}