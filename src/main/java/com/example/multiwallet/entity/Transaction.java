package com.example.multiwallet.entity;

import com.example.multiwallet.entity.enums.TransactionStatus;
import com.example.multiwallet.entity.enums.TransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "from_wallet_id")
    private Wallet fromWallet;

    @ManyToOne

    @JoinColumn(name = "to_wallet_id")
    private Wallet toWallet;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(nullable = true , length = 255)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Transaction(){}

    public UUID getId(){
        return id;
    }

    public Wallet getFromWallet(){
        return fromWallet;
    }

    public Wallet getToWallet(){
        return toWallet;
    }

    public BigDecimal getAmount(){
        return amount;
    }

    public TransactionType getType(){
        return type;
    }

    public TransactionStatus getStatus(){
        return status;
    }

    public String getDescription(){
        return description;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }

    public void setId(UUID id){
        this.id=id;
    }

    public void setFromWallet(Wallet fromWallet){
        this.fromWallet=fromWallet;
    }

    public void setToWallet(Wallet toWallet){
        this.toWallet=toWallet;
    }

    public void setAmount(BigDecimal amount){
        this.amount=amount;
    }

    public void setType(TransactionType type){
        this.type=type;
    }

    public void setStatus(TransactionStatus status){
        this.status=status;
    }
    public void setDescription(String description){
        this.description=description;
    }
    @PrePersist
    public void onCreatedAt(){
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdatedAt(){
        this.updatedAt=LocalDateTime.now();
    }
}
