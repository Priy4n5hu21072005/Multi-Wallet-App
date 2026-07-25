package com.example.multiwallet.service;

import com.example.multiwallet.dto.transaction.DepositRequest;
import com.example.multiwallet.dto.transaction.TransactionResponse;
import com.example.multiwallet.dto.transaction.TransferRequest;
import com.example.multiwallet.dto.transaction.WithdrawRequest;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponse depositFakeMoney(UUID walletId, DepositRequest request);
    TransactionResponse withdrawFakeMoney(UUID walletId, WithdrawRequest request);
    TransactionResponse transferMoney(TransferRequest request);
    List<TransactionResponse> getTransactionsForWallet(UUID walletId);
    List<TransactionResponse> getTransactionsForUser(UUID userId);
}
