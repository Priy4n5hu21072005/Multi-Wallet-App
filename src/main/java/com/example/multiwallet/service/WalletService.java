package com.example.multiwallet.service;

import com.example.multiwallet.entity.Wallet;

import java.util.UUID;

public interface WalletService {
    Wallet createWallet(UUID id , Wallet wallet);
}
