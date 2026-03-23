package com.CardMaster.modules.cpl.dto;

import com.CardMaster.modules.cpl.enums.FeeType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeeConfigResponseDto {
    private Long feeId;
    private Long productId;
    private FeeType feeType;
    private Double amount;
}