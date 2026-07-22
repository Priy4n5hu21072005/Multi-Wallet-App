package com.example.multiwallet.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OtpUtill {
    private final SecureRandom random = new SecureRandom();
    public String genrateOtp(){
        return String.format("%06d",random.nextInt(1000000));

    }
}
