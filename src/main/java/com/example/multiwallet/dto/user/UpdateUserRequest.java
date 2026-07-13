package com.example.multiwallet.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @Size(max = 100)
    private String fullName;

    @Size(max = 15)
    private String phoneNumber;
}