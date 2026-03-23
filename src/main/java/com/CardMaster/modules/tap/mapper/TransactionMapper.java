package com.CardMaster.modules.tap.mapper;

import com.CardMaster.modules.tap.dto.TransactionDto;
import com.CardMaster.modules.tap.entity.Transaction;
import com.CardMaster.modules.cias.entity.CardAccount;
import com.CardMaster.modules.cias.repository.CardAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final CardAccountRepository accountRepository;

    // DTO → Entity
    public Transaction toEntity(TransactionDto dto) {
        CardAccount account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + dto.getAccountId()));

        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(dto.getAmount());
        tx.setCurrency(dto.getCurrency());
        tx.setMerchant(dto.getMerchant());
        tx.setChannel(dto.getChannel());
        tx.setTransactionDate(dto.getTransactionDate());
        tx.setStatus(dto.getStatus());
        return tx;
    }

    // Entity → DTO
    public TransactionDto toDTO(Transaction tx) {
        TransactionDto dto = new TransactionDto();
        dto.setTransactionId(tx.getTransactionId());
        dto.setAccountId(tx.getAccount().getAccountId());
        dto.setAmount(tx.getAmount());
        dto.setCurrency(tx.getCurrency());
        dto.setMerchant(tx.getMerchant());
        dto.setChannel(tx.getChannel());
        tx.setTransactionDate(tx.getTransactionDate());
        dto.setStatus(tx.getStatus());
        return dto;
    }

}
