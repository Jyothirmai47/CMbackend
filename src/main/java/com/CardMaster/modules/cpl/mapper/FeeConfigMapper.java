package com.CardMaster.modules.cpl.mapper;

import com.CardMaster.modules.cpl.dto.FeeConfigRequestDto;
import com.CardMaster.modules.cpl.dto.FeeConfigResponseDto;
import com.CardMaster.modules.cpl.entity.CardProduct;
import com.CardMaster.modules.cpl.entity.FeeConfig;
import org.springframework.stereotype.Component;

@Component
public class FeeConfigMapper {

    public FeeConfig toEntity(FeeConfigRequestDto req, CardProduct product) {
        FeeConfig e = new FeeConfig();
        e.setProduct(product);
        e.setFeeType(req.getFeeType());
        e.setAmount(req.getAmount());
        return e;
    }

    public void updateEntity(FeeConfig e, FeeConfigRequestDto req, CardProduct maybeNewProduct) {
        if (maybeNewProduct != null) e.setProduct(maybeNewProduct);
        if (req.getFeeType() != null) e.setFeeType(req.getFeeType());
        if (req.getAmount() != null) e.setAmount(req.getAmount());
    }

    public FeeConfigResponseDto toResponse(FeeConfig e) {
        return FeeConfigResponseDto.builder()
                .feeId(e.getFeeId())
                .productId(e.getProduct().getProductId())
                .feeType(e.getFeeType())
                .amount(e.getAmount())
                .build();
    }
}
