package com.example.multiwallet.controller;

import com.example.multiwallet.dto.auth.LoginRequest;
import com.example.multiwallet.dto.auth.LoginResponse;
import com.example.multiwallet.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
private final AuthService authService;
public AuthController(AuthService authService){
    this.authService=authService;
}
@PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request){
    return authService.login(request);
}
}
