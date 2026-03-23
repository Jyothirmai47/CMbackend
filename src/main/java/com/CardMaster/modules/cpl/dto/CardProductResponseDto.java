package com.CardMaster.modules.cpl.dto;

import com.CardMaster.modules.cpl.enums.CardCategory;
import com.CardMaster.modules.cpl.enums.ProductStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardProductResponseDto {
    private Long productId;
    private String name;
    private CardCategory category;
    private Double interestRate;
    private Double annualFee;
    private ProductStatus status;
}