package com.CardMaster.modules.cias.mapper;

import com.CardMaster.modules.cias.dto.CardAccountResponseDto;
import com.CardMaster.modules.cias.entity.CardAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardAccountMapper {

    // Entity -> Response DTO
    public CardAccountResponseDto toDTO(CardAccount account) {
        CardAccountResponseDto dto = new CardAccountResponseDto();
        dto.setAccountId(account.getAccountId());
        dto.setCardId(account.getCard().getCardId()); // only reference cardId
        dto.setApplicationId(account.getCard().getApplication().getApplicationId());
        dto.setCreditLimit(account.getCreditLimit());
        dto.setAvailableLimit(account.getAvailableLimit());
        dto.setOpenDate(account.getOpenDate());
        dto.setStatus(account.getStatus().name());
        return dto;
    }
}
