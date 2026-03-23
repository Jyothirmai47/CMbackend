package com.CardMaster.modules.cias.mapper;

import com.CardMaster.modules.cias.enums.CardStatus;
import com.CardMaster.modules.cias.dto.CardRequestDto;
import com.CardMaster.modules.cias.dto.CardResponseDto;
import com.CardMaster.modules.cias.entity.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardMapper {

    // Entity -> Response DTO
    public CardResponseDto toDTO(Card card) {
        CardResponseDto dto = new CardResponseDto();
        dto.setCardId(card.getCardId());
        dto.setApplicationId(card.getApplication().getApplicationId());
        dto.setCustomerId(card.getCustomer().getCustomerId());
        dto.setProductId(card.getProduct().getProductId());
        dto.setMaskedCardNumber(card.getMaskedCardNumber());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setStatus(card.getStatus().name());
        return dto;
    }
}
