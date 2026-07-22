package com.example.multiwallet.service.impl;

import com.example.multiwallet.dto.auth.LoginRequest;
import com.example.multiwallet.dto.auth.LoginResponse;
import com.example.multiwallet.dto.otp.SendOtpRequest;
import com.example.multiwallet.dto.otp.VerifyOtpRequest;
import com.example.multiwallet.entity.EmailOtp;
import com.example.multiwallet.entity.User;
import com.example.multiwallet.entity.enums.OtpPurpose;
import com.example.multiwallet.exception.EmailAlreadyExists;
import com.example.multiwallet.repository.EmailOtpRepository;
import com.example.multiwallet.repository.UserRepository;
import com.example.multiwallet.security.JwtUtil;
import com.example.multiwallet.security.OtpUtill;
import com.example.multiwallet.service.AuthService;
import com.example.multiwallet.service.MailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmailOtpRepository emailOtpRepository;
    private final MailService mailService;
    private final OtpUtill otpUtill;
    private final UserRepository userRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil,
                           EmailOtpRepository emailOtpRepository,
                           MailService mailService,
                           OtpUtill otpUtill,
                           UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.emailOtpRepository=emailOtpRepository;
        this.mailService=mailService;
        this.otpUtill=otpUtill;
        this.userRepository=userRepository;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        User user = (User) authentication.getPrincipal();

        String token = jwtUtil.generateToken(user);

        return new LoginResponse(token);
    }

    @Override
    public void sendRegistrationOtp(SendOtpRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExists("Email Already Exists");
        }
        String otp = otpUtill.genrateOtp();

        EmailOtp emailOtp = new EmailOtp();

        emailOtp.setEmail(request.getEmail());
        emailOtp.setOtp(otp);
        emailOtp.setPurpose(OtpPurpose.EmailVerification);
        emailOtp.setVerified(false);
        emailOtp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        emailOtpRepository.save(emailOtp);

        mailService.sentOtp(request.getEmail(),otp);

    }

    @Override
    public boolean verifyOtp(VerifyOtpRequest request) {
        EmailOtp emailOtp = emailOtpRepository.findTopByEmailAndPurposeOrderByCreatedAtDesc(request.getEmail(),OtpPurpose.EmailVerification)
                .orElseThrow(()->new RuntimeException("Otp Not Found"));
        if(emailOtp.isVerified()) {throw new RuntimeException("otp already used");}
        if (LocalDateTime.now().isAfter(emailOtp.getExpiryTime())){throw new RuntimeException("otp is expired");}
        if(!emailOtp.getOtp().equals(request.getOtp())){
            throw new RuntimeException("Invalid OTP");
        }
        emailOtp.setVerified(true);
        emailOtpRepository.save(emailOtp);
        return true;
    }
}