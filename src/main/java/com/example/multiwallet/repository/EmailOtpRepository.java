package com.example.multiwallet.repository;

import com.example.multiwallet.entity.EmailOtp;
import com.example.multiwallet.entity.enums.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, UUID> {

    Optional<EmailOtp> findTopByEmailAndPurposeOrderByCreatedAtDesc(
            String email,
            OtpPurpose purpose
    );

}