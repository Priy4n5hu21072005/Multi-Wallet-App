package com.example.multiwallet.service.impl;

import com.example.multiwallet.dto.auth.*;
import com.example.multiwallet.dto.otp.SendOtpRequest;
import com.example.multiwallet.dto.otp.VerifyOtpRequest;
import com.example.multiwallet.dto.user.RegisterUserRequest;
import com.example.multiwallet.dto.user.UserResponse;
import com.example.multiwallet.entity.EmailOtp;
import com.example.multiwallet.entity.User;
import com.example.multiwallet.entity.Wallet;
import com.example.multiwallet.entity.enums.OtpPurpose;
import com.example.multiwallet.exception.EmailAlreadyExists;
import com.example.multiwallet.exception.UserNotFound;
import com.example.multiwallet.mapper.UserMapper;
import com.example.multiwallet.repository.EmailOtpRepository;
import com.example.multiwallet.repository.UserRepository;
import com.example.multiwallet.repository.WalletRepository;
import com.example.multiwallet.security.JwtUtil;
import com.example.multiwallet.security.OtpUtill;
import com.example.multiwallet.service.AuthService;
import com.example.multiwallet.service.MailService;
import com.example.multiwallet.service.RedisService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmailOtpRepository emailOtpRepository;
    private final MailService mailService;
    private final OtpUtill otpUtill;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RedisService redisService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil,
                           EmailOtpRepository emailOtpRepository,
                           MailService mailService,
                           OtpUtill otpUtill,
                           UserRepository userRepository,
                           WalletRepository walletRepository,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper,
                           RedisService redisService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.emailOtpRepository = emailOtpRepository;
        this.mailService = mailService;
        this.otpUtill = otpUtill;
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.redisService = redisService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
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
        if (userRepository.existsByEmail(request.getEmail())) {
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

        mailService.sendOtp(request.getEmail(), otp, "Registration");
    }

    @Override
    @Transactional
    public UserResponse verifyRegistrationOtp(VerifyOtpRequest request) {
        // Verify OTP
        verifyOtp(request);

        // Check if pending registration details exist in Redis
        RegisterUserRequest pendingRequest = redisService.getPendingUsers(request.getEmail());

        User user = new User();
        user.setEmail(request.getEmail());

        if (pendingRequest != null) {
            user.setFullName(pendingRequest.getFullName());
            user.setPassword(pendingRequest.getPassword()); // Already encoded or will be ensured
            user.setPhoneNumber(pendingRequest.getPhoneNumber());
            redisService.deletePendingUser(request.getEmail());
        } else {
            // Fallback if details not in Redis
            user.setFullName(request.getEmail().split("@")[0]);
            user.setPassword(passwordEncoder.encode("DefaultPass123!"));
        }

        // Save User
        User savedUser = userRepository.save(user);

        // Auto-create default primary wallet
        Wallet defaultWallet = new Wallet();
        defaultWallet.setUser(savedUser);
        defaultWallet.setWalletName("Primary Wallet");
        defaultWallet.setBalance(BigDecimal.ZERO);
        defaultWallet.setCurrency("INR");
        defaultWallet.setDefault(true);
        walletRepository.save(defaultWallet);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public boolean verifyOtp(VerifyOtpRequest request) {
        EmailOtp emailOtp = emailOtpRepository.findTopByEmailAndPurposeOrderByCreatedAtDesc(
                        request.getEmail(), OtpPurpose.EmailVerification)
                .orElseGet(() -> emailOtpRepository.findTopByEmailAndPurposeOrderByCreatedAtDesc(
                        request.getEmail(), OtpPurpose.ForgetPassword)
                        .orElseThrow(() -> new RuntimeException("OTP not found for email: " + request.getEmail())));

        if (emailOtp.isVerified()) {
            throw new RuntimeException("OTP has already been used");
        }
        if (LocalDateTime.now().isAfter(emailOtp.getExpiryTime())) {
            throw new RuntimeException("OTP has expired. Please request a new one.");
        }
        if (!emailOtp.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP provided");
        }

        emailOtp.setVerified(true);
        emailOtpRepository.save(emailOtp);
        return true;
    }

    @Override
    public void resendOtp(ResendOtpRequest request) {
        String otp = otpUtill.genrateOtp();

        EmailOtp emailOtp = new EmailOtp();
        emailOtp.setEmail(request.getEmail());
        emailOtp.setOtp(otp);
        emailOtp.setPurpose(request.getPurpose() != null ? request.getPurpose() : OtpPurpose.EmailVerification);
        emailOtp.setVerified(false);
        emailOtp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        emailOtpRepository.save(emailOtp);

        mailService.sendOtp(request.getEmail(), otp, "Resend " + emailOtp.getPurpose().name());
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFound("User not found with email: " + request.getEmail()));

        String otp = otpUtill.genrateOtp();

        EmailOtp emailOtp = new EmailOtp();
        emailOtp.setEmail(user.getEmail());
        emailOtp.setOtp(otp);
        emailOtp.setPurpose(OtpPurpose.ForgetPassword);
        emailOtp.setVerified(false);
        emailOtp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        emailOtpRepository.save(emailOtp);

        mailService.sendOtp(user.getEmail(), otp, "Password Reset");
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFound("User not found with email: " + request.getEmail()));

        VerifyOtpRequest verifyRequest = new VerifyOtpRequest();
        verifyRequest.setEmail(request.getEmail());
        verifyRequest.setOtp(request.getOtp());
        verifyOtp(verifyRequest);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}