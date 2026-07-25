package com.example.multiwallet.service.impl;

import com.example.multiwallet.dto.user.RegisterUserRequest;
import com.example.multiwallet.dto.user.UpdateUserRequest;
import com.example.multiwallet.dto.user.UserResponse;
import com.example.multiwallet.entity.EmailOtp;
import com.example.multiwallet.entity.User;
import com.example.multiwallet.entity.enums.OtpPurpose;
import com.example.multiwallet.exception.EmailAlreadyExists;
import com.example.multiwallet.exception.PhoneNumberAlreadyExists;
import com.example.multiwallet.exception.UserNotFound;
import com.example.multiwallet.mapper.UserMapper;
import com.example.multiwallet.repository.EmailOtpRepository;
import com.example.multiwallet.repository.UserRepository;
import com.example.multiwallet.security.OtpUtill;
import com.example.multiwallet.service.MailService;
import com.example.multiwallet.service.RedisService;
import com.example.multiwallet.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final MailService mailService;
    private final OtpUtill otpUtill;
    private final EmailOtpRepository emailOtpRepository;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           RedisService redisService,
                           MailService mailService,
                           OtpUtill otpUtill,
                           EmailOtpRepository emailOtpRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.mailService = mailService;
        this.otpUtill = otpUtill;
        this.emailOtpRepository = emailOtpRepository;
    }

    @Override
    public void registerUser(RegisterUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExists("Email already exists");
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new PhoneNumberAlreadyExists("Phone Number Already Exists");
            }
        }

        // Keep raw password temporarily in request for Redis, but encode it when saving
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        String otp = otpUtill.genrateOtp();

        // Save encoded password in request for Redis pending storage
        RegisterUserRequest pendingRequest = new RegisterUserRequest();
        pendingRequest.setFullName(request.getFullName());
        pendingRequest.setEmail(request.getEmail());
        pendingRequest.setPhoneNumber(request.getPhoneNumber());
        pendingRequest.setPassword(encodedPassword);

        redisService.savePendingUsers(pendingRequest, otp);

        // Also save to EmailOtp entity in database
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
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("User Not Found"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFound("User Not Found with email: " + email));
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("User Not Found"));

        // Duplicate Email check
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new EmailAlreadyExists("Email already exists");
            }
        }

        // Duplicate Phone check
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new PhoneNumberAlreadyExists("Phone Number Already Exists");
            }
        }
        userMapper.UpdateUserFromRequest(request, user);

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(UUID id) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFound("User Not Found");
        }

        userRepository.deleteById(id);
    }
}