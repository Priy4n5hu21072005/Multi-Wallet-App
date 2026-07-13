package com.example.multiwallet.dto.user;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

import java.time.LocalDateTime;
@Getter
@Setter
public class UserResponse {
    private UUID id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
