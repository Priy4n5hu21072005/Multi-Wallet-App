package com.example.multiwallet.dto.wallet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateWalletRequest {
    @NotBlank
    @Size(max = 254)
    private String walletName;

    @NotBlank
    @PositiveOrZero
    private BigDecimal balance;

    @NotNull
    private UUID user_id;
}
