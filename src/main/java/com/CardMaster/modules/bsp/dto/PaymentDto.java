package com.CardMaster.modules.bsp.dto;

import com.CardMaster.modules.bsp.enums.PaymentMethod;
import com.CardMaster.modules.bsp.enums.PaymentStatus;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long paymentId;
    private Long accountId;
    private Long statementId;
    private Double amount;
    private LocalDateTime paymentDate;
    private PaymentMethod method;
    private PaymentStatus status;
}
