package com.example.multiwallet.service.impl;

import com.example.multiwallet.service.MailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;

    public MailServiceImpl(JavaMailSender mailSender){
        this.mailSender=mailSender;
    }

    @Override
    public void sentOtp(String to, String otp) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("Multiwallet email verification");
        mailMessage.setText("""
                Hello Sir/Madam, Welcome to Multiwallet your otp for multiwallet is %s 
                this is valid for 5 minutes 
                        ~Team MultiWallet""".formatted(otp));
        mailSender.send(mailMessage);
    }
}
