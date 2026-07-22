package com.example.multiwallet.service;

import org.springframework.context.annotation.Bean;


public interface MailService {
    void sentOtp(String to , String otp);
}
