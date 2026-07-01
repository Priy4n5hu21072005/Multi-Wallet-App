package com.example.multiwallet.service.impl;

import com.example.multiwallet.entity.User;
import com.example.multiwallet.entity.Wallet;
import com.example.multiwallet.repository.UserRepository;
import com.example.multiwallet.repository.WalletRepository;
import com.example.multiwallet.service.WalletService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public WalletServiceImpl(WalletRepository walletRepository , UserRepository userRepository){
        this.walletRepository=walletRepository;
        this.userRepository=userRepository;
    }


    @Override
    public Wallet createWallet(UUID id, Wallet wallet) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("user not found"));
        wallet.setUser(user);
        return walletRepository.save(wallet);

    }
}
