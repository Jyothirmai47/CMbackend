package com.CardMaster.modules.cpl.dto;

import com.CardMaster.modules.cpl.enums.FeeType;
import lombok.Data;

@Data
public class FeeConfigRequestDto {
    private Long productId;
    private FeeType feeType;
    private Double amount;
}