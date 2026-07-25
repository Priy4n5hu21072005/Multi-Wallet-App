package com.example.multiwallet.service.impl;

import com.example.multiwallet.dto.wallet.WalletRequest;
import com.example.multiwallet.dto.wallet.WalletResponse;
import com.example.multiwallet.entity.User;
import com.example.multiwallet.entity.Wallet;
import com.example.multiwallet.repository.UserRepository;
import com.example.multiwallet.repository.WalletRepository;
import com.example.multiwallet.service.QrCodeService;
import com.example.multiwallet.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final QrCodeService qrCodeService;

    public WalletServiceImpl(WalletRepository walletRepository,
                             UserRepository userRepository,
                             QrCodeService qrCodeService) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.qrCodeService = qrCodeService;
    }

    @Override
    @Transactional
    public WalletResponse createWallet(UUID userId, WalletRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        boolean hasWallets = !walletRepository.findByUserId(userId).isEmpty();

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setWalletName(request.getWalletName());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");
        wallet.setDefault(!hasWallets); // First wallet created is default

        Wallet saved = walletRepository.save(wallet);
        return mapToResponse(saved);
    }

    @Override
    public List<WalletResponse> getWalletsByUser(UUID userId) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        return wallets.stream().map(this::mapToResponse).toList();
    }

    @Override
    public WalletResponse getWalletById(UUID id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found with id: " + id));
        return mapToResponse(wallet);
    }

    @Override
    public WalletResponse getWalletByNumber(String walletNumber) {
        Wallet wallet = walletRepository.findByWalletNumber(walletNumber)
                .orElseThrow(() -> new RuntimeException("Wallet not found with number: " + walletNumber));
        return mapToResponse(wallet);
    }

    @Override
    @Transactional
    public WalletResponse updateWallet(UUID walletId, WalletRequest request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found with id: " + walletId));

        if (request.getWalletName() != null && !request.getWalletName().isBlank()) {
            wallet.setWalletName(request.getWalletName());
        }
        if (request.getCurrency() != null && !request.getCurrency().isBlank()) {
            wallet.setCurrency(request.getCurrency());
        }

        Wallet updated = walletRepository.save(wallet);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteWallet(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found with id: " + walletId));

        if (wallet.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("Cannot delete wallet with non-zero balance: " + wallet.getBalance());
        }

        if (wallet.isDefault()) {
            throw new RuntimeException("Cannot delete primary default wallet");
        }

        walletRepository.delete(wallet);
    }

    @Override
    public String generateQrCodeForWallet(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found with id: " + walletId));

        // Format payload JSON string for QR Code scanning
        String qrPayload = String.format(
                "{\"walletId\":\"%s\",\"walletNumber\":\"%s\",\"userEmail\":\"%s\",\"userName\":\"%s\"}",
                wallet.getId(),
                wallet.getWalletNumber(),
                wallet.getUser().getEmail(),
                wallet.getUser().getFullName()
        );

        return qrCodeService.generateQrCodeBase64(qrPayload, 300, 300);
    }

    private WalletResponse mapToResponse(Wallet wallet) {
        return WalletResponse.builder()
                .id(wallet.getId())
                .walletNumber(wallet.getWalletNumber())
                .walletName(wallet.getWalletName())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .isDefault(wallet.isDefault())
                .userId(wallet.getUser().getId())
                .userEmail(wallet.getUser().getEmail())
                .createdAt(wallet.getCreatedAt())
                .updatedAt(wallet.getUpdatedAt())
                .build();
    }
}
