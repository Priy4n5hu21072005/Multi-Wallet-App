package com.example.multiwallet.controller;
import com.example.multiwallet.dto.user.RegisterUserRequest;
import com.example.multiwallet.dto.user.UserResponse;
import com.example.multiwallet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }
    @PostMapping("/register")
    public UserResponse registerUser(@Valid @RequestBody RegisterUserRequest request){
        return userService.registerUser(request);
    }
    @GetMapping
    public List<UserResponse> getAllUser(){
        return userService.getAllUsers();
    }
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable UUID id){
        return userService.getUserById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable UUID id){
         userService.deleteUser(id);
    }
}
