package com.example.multiwallet.service;

public interface MailService {
    void sentOtp(String to, String otp);
    void sendOtp(String to, String otp, String purpose);
    void sendFinancialInsightEmail(String to, String userName, String insightsHtml);
}
