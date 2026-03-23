package com.CardMaster.modules.bsp.mapper;

import com.CardMaster.modules.bsp.dto.PaymentDto;
import com.CardMaster.modules.bsp.entity.Statement;
import com.CardMaster.modules.bsp.entity.Payment;
import com.CardMaster.modules.cias.entity.CardAccount;
import com.CardMaster.modules.bsp.repository.StatementRepository;
import com.CardMaster.modules.cias.repository.CardAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final CardAccountRepository accountRepository;
    private final StatementRepository statementRepository;

    // DTO → Entity
    public Payment toEntity(PaymentDto dto) {
        CardAccount account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + dto.getAccountId()));

        Payment p = new Payment();
        p.setAccount(account);
        if (dto.getStatementId() != null) {
            Statement statement = statementRepository.findById(dto.getStatementId())
                    .orElseThrow(() -> new IllegalArgumentException("Statement not found with ID: " + dto.getStatementId()));
            p.setStatement(statement);
        }
        p.setAmount(dto.getAmount());
        p.setPaymentDate(dto.getPaymentDate());
        p.setMethod(dto.getMethod());
        p.setStatus(dto.getStatus());
        return p;
    }

    // Entity → DTO
    public PaymentDto toDTO(Payment p) {
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(p.getPaymentId());
        dto.setAccountId(p.getAccount().getAccountId());
        dto.setStatementId(p.getStatement() != null ? p.getStatement().getStatementId() : null);
        dto.setAmount(p.getAmount());
        p.setPaymentDate(p.getPaymentDate());
        dto.setMethod(p.getMethod());
        dto.setStatus(p.getStatus());
        return dto;
    }
}
