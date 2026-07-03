package com.example.multiwallet.controller;

import com.example.multiwallet.entity.Wallet;
import com.example.multiwallet.service.WalletService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // Get Wallet By Id
    @GetMapping("/{walletId}")
    public Wallet getWalletById(@PathVariable UUID walletId) {
        return walletService.getWalletById(walletId);
    }

    // Update Wallet
    @PutMapping("/{walletId}")
    public Wallet updateWallet(@PathVariable UUID walletId,
                               @RequestBody Wallet wallet) {
        return walletService.updateWallet(walletId, wallet);
    }

    // Delete Wallet
    @DeleteMapping("/{walletId}")
    public void deleteWallet(@PathVariable UUID walletId) {
        walletService.deleteWallet(walletId);
    }
}