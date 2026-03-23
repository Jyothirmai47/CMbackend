package com.CardMaster.modules.cpl.service;

import com.CardMaster.modules.cpl.dto.FeeConfigRequestDto;
import com.CardMaster.modules.cpl.dto.FeeConfigResponseDto;
import com.CardMaster.modules.cpl.mapper.FeeConfigMapper;
import com.CardMaster.modules.cpl.entity.CardProduct;
import com.CardMaster.modules.cpl.entity.FeeConfig;
import com.CardMaster.modules.cpl.repository.CardProductRepository;
import com.CardMaster.modules.cpl.repository.FeeConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeeConfigService {

    private final FeeConfigRepository feeRepo;
    private final CardProductRepository productRepo;
    private final FeeConfigMapper mapper;

    public FeeConfigResponseDto addFee(FeeConfigRequestDto req) {
        // NOTE: We must load product first to pass as 2nd argument to mapper
        CardProduct product = productRepo.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        FeeConfig entity = mapper.toEntity(req, product); // <-- 2 arguments
        FeeConfig saved = feeRepo.save(entity);
        return mapper.toResponse(saved);
    }

    public List<FeeConfigResponseDto> getFeesByProduct(Long productId) {
        return feeRepo.findByProduct_ProductId(productId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}