package com.example.multiwallet.repository;

import com.example.multiwallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface WalletRepository extends JpaRepository<Wallet,UUID> {
}
