package com.example.multiwallet.service;

import com.example.multiwallet.entity.Wallet;

import java.util.List;
import java.util.UUID;

public interface WalletService {
    Wallet createWallet(UUID id , Wallet wallet);
    List<Wallet> getWalletsByUser(UUID userId);
    Wallet getWalletById(UUID id);
    Wallet updateWallet(UUID walletId , Wallet wallet);
    void deleteWallet(UUID walletId);
}
