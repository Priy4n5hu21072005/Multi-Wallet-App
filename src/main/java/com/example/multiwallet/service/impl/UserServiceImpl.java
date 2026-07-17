package com.example.multiwallet.service.impl;

import com.example.multiwallet.dto.user.RegisterUserRequest;
import com.example.multiwallet.dto.user.UpdateUserRequest;
import com.example.multiwallet.dto.user.UserResponse;
import com.example.multiwallet.entity.User;
import com.example.multiwallet.exception.EmailAlreadyExists;
import com.example.multiwallet.exception.PhoneNumberAlreadyExists;
import com.example.multiwallet.exception.UserNotFound;
import com.example.multiwallet.mapper.UserMapper;
import com.example.multiwallet.repository.UserRepository;
import com.example.multiwallet.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse registerUser(RegisterUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExists("Email already exists");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())){
            throw new PhoneNumberAlreadyExists("Phone Number Already Exists");
        }

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);

    }

    @Override
    public UserResponse getUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("User Not Found"));

        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("User Not Found"));

        // Duplicate Email check
        if(request.getEmail() != null && !request.getEmail().equals(user.getEmail())){
            if(userRepository.existsByEmail(request.getEmail())){
                throw new EmailAlreadyExists("email Already exists");
            }
        }

        // Duplicate Phone check
        if(request.getPhoneNumber() != null && !request.getPhoneNumber().equals(user.getPhoneNumber())){
            if (userRepository.existsByPhoneNumber(request.getPhoneNumber())){
                throw new PhoneNumberAlreadyExists("Phone Number Already Exists");
            }
        }
        userMapper.UpdateUserFromRequest(request,user);

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(UUID id) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFound("User Not Found");
        }

        userRepository.deleteById(id);
    }
}