package com.example.multiwallet.controller;

import com.example.multiwallet.entity.Wallet;
import com.example.multiwallet.service.WalletService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/{userId}/wallets")
public class WalletController {
    private final WalletService walletService;
    public WalletController(WalletService walletService){
        this.walletService=walletService;
    }
    @PostMapping
    public Wallet createWallet(@PathVariable  UUID userId ,@RequestBody Wallet wallet){
                return walletService.createWallet(userId, wallet);
    }
}
