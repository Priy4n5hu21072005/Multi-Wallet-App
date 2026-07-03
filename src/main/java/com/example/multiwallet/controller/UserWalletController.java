package com.example.multiwallet.controller;

import com.example.multiwallet.entity.Wallet;
import com.example.multiwallet.service.WalletService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/wallets")
public class UserWalletController {

    private final WalletService walletService;

    public UserWalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // Create Wallet
    @PostMapping
    public Wallet createWallet(@PathVariable UUID userId,
                               @RequestBody Wallet wallet) {
        return walletService.createWallet(userId, wallet);
    }

    // Get All Wallets of a User
    @GetMapping
    public List<Wallet> getWalletsByUser(@PathVariable UUID userId) {
        return walletService.getWalletsByUser(userId);
    }
}