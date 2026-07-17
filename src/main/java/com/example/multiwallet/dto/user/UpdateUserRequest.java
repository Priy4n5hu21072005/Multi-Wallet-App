package com.example.multiwallet.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @Size(max = 100)
    private String fullName;

    @Size(min = 10, max = 15)
    private String phoneNumber;

    @Email
    @Size(max=254)
    private String email;
}