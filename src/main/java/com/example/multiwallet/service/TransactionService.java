package com.example.multiwallet.service;

import com.example.multiwallet.entity.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionService {
    Transaction addMoney(UUID walletId , BigDecimal amount);
}
