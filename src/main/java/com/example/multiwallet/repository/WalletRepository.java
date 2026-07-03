package com.example.multiwallet.repository;

import com.example.multiwallet.entity.User;
import com.example.multiwallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface WalletRepository extends JpaRepository<Wallet,UUID> {
     List<Wallet> findByUserId(UUID userId);

}
