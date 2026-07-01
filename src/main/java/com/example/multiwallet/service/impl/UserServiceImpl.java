package com.example.multiwallet.service.impl;
import com.example.multiwallet.entity.User;
import com.example.multiwallet.repository.UserRepository;
import com.example.multiwallet.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public User registerUser(User user) {
        if(userRepository.existsByEmail((user.getEmail()))){
            throw new RuntimeException("Email Already exist");
        }
        return userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User Not Found!"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
