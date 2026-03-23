package com.CardMaster.modules.cpl.service;

import com.CardMaster.modules.cpl.enums.ProductStatus;
import com.CardMaster.modules.cpl.dto.CardProductRequestDto;
import com.CardMaster.modules.cpl.dto.CardProductResponseDto;
import com.CardMaster.modules.cpl.mapper.CardProductMapper;
import com.CardMaster.modules.cpl.entity.CardProduct;
import com.CardMaster.modules.cpl.repository.CardProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardProductService {

    private final CardProductRepository repository;
    private final CardProductMapper mapper;

    public CardProductResponseDto create(CardProductRequestDto request) {
        CardProduct entity = mapper.toEntity(request);

        // Set default status since request does NOT carry it anymore
        entity.setStatus(ProductStatus.ACTIVE);   // ✅ default status

        CardProduct saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public List<CardProductResponseDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public CardProductResponseDto findById(Long id) {
        CardProduct entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapper.toResponse(entity);
    }
}