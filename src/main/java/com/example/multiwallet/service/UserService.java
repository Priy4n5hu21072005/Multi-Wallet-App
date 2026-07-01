package com.example.multiwallet.service;
import com.example.multiwallet.entity.User;
import java.util.UUID;
import java.util.List;
public interface UserService {
    User registerUser(User user);
    User getUserById(UUID id);
    List<User> getAllUsers();
    void deleteUser(UUID id);
}