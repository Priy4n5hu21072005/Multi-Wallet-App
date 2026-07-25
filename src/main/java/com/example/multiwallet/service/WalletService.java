package com.example.multiwallet.service;

import com.example.multiwallet.dto.wallet.WalletRequest;
import com.example.multiwallet.dto.wallet.WalletResponse;

import java.util.List;
import java.util.UUID;

public interface WalletService {
    WalletResponse createWallet(UUID userId, WalletRequest request);
    List<WalletResponse> getWalletsByUser(UUID userId);
    WalletResponse getWalletById(UUID id);
    WalletResponse getWalletByNumber(String walletNumber);
    WalletResponse updateWallet(UUID walletId, WalletRequest request);
    void deleteWallet(UUID walletId);
    String generateQrCodeForWallet(UUID walletId);
}
