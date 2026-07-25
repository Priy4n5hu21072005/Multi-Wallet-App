package com.example.multiwallet.controller;

import com.example.multiwallet.dto.wallet.WalletRequest;
import com.example.multiwallet.dto.wallet.WalletResponse;
import com.example.multiwallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@PathVariable UUID userId,
                                                       @Valid @RequestBody WalletRequest request) {
        return ResponseEntity.ok(walletService.createWallet(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<WalletResponse>> getWalletsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(walletService.getWalletsByUser(userId));
    }
}