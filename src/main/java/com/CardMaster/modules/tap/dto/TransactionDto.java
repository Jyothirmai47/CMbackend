package com.CardMaster.modules.tap.dto;

import com.CardMaster.modules.tap.enums.TransactionChannel;
import com.CardMaster.modules.tap.enums.TransactionStatus;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long transactionId;
    private Long accountId;
    private Double amount;
    private String currency;
    private String merchant;
    private TransactionChannel channel;
    private LocalDateTime transactionDate;
    private TransactionStatus status;
}
