package com.example.multiwallet.service.impl;

import com.example.multiwallet.entity.User;
import com.example.multiwallet.entity.Wallet;
import com.example.multiwallet.repository.UserRepository;
import com.example.multiwallet.repository.WalletRepository;
import com.example.multiwallet.service.WalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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

    @Override
    public List<Wallet> getWalletsByUser(UUID userId) {
        return walletRepository.findByUserId(userId);
    }

    @Override
    public Wallet getWalletById(UUID id) {
        return walletRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Wallet Not Found"));
    }

    @Override
    public Wallet updateWallet(UUID walletId, Wallet updateWallet) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(()->new RuntimeException("Wallet not found"));
        wallet.setWalletName(updateWallet.getWalletName());
        return walletRepository.save(wallet);

    }

    @Override
    public void deleteWallet(UUID walletId) {
            Wallet wallet =walletRepository.findById(walletId)
                    .orElseThrow(()->new RuntimeException("Wallet not found"));
            if(wallet.getBalance().compareTo(BigDecimal.ZERO)>0){
                throw new RuntimeException("Wallet must have more than 0 rupees");
            }
            walletRepository.delete(wallet);
    }


}
