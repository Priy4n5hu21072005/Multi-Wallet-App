package com.example.multiwallet.controller;

import com.example.multiwallet.dto.wallet.WalletRequest;
import com.example.multiwallet.dto.wallet.WalletResponse;
import com.example.multiwallet.entity.User;
import com.example.multiwallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@AuthenticationPrincipal User currentUser,
                                                       @Valid @RequestBody WalletRequest request) {
        WalletResponse response = walletService.createWallet(currentUser.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WalletResponse>> getMyWallets(@AuthenticationPrincipal User currentUser) {
        List<WalletResponse> responses = walletService.getWalletsByUser(currentUser.getId());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponse> getWalletById(@PathVariable UUID walletId) {
        WalletResponse response = walletService.getWalletById(walletId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/number/{walletNumber}")
    public ResponseEntity<WalletResponse> getWalletByNumber(@PathVariable String walletNumber) {
        WalletResponse response = walletService.getWalletByNumber(walletNumber);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{walletId}")
    public ResponseEntity<WalletResponse> updateWallet(@PathVariable UUID walletId,
                                                       @Valid @RequestBody WalletRequest request) {
        WalletResponse response = walletService.updateWallet(walletId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{walletId}")
    public ResponseEntity<String> deleteWallet(@PathVariable UUID walletId) {
        walletService.deleteWallet(walletId);
        return ResponseEntity.ok("Wallet deleted successfully");
    }

    @GetMapping("/{walletId}/qr")
    public ResponseEntity<Map<String, String>> getWalletQrCode(@PathVariable UUID walletId) {
        String base64Qr = walletService.generateQrCodeForWallet(walletId);
        Map<String, String> result = new HashMap<>();
        result.put("walletId", walletId.toString());
        result.put("qrCodeBase64", base64Qr);
        return ResponseEntity.ok(result);
    }
}