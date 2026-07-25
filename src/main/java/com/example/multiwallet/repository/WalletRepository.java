package com.example.multiwallet.repository;

import com.example.multiwallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    List<Wallet> findByUserId(UUID userId);
    Optional<Wallet> findByWalletNumber(String walletNumber);
    Optional<Wallet> findByUserIdAndIsDefaultTrue(UUID userId);
    boolean existsByWalletNumber(String walletNumber);
}
