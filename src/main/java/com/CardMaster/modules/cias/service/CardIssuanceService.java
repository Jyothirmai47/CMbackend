package com.CardMaster.modules.cias.service;

import com.CardMaster.modules.cias.enums.CardStatus;
import com.CardMaster.modules.cau.enums.UnderwritingDecisionType;
import com.CardMaster.modules.cau.repository.UnderwritingDecisionRepository;
import com.CardMaster.modules.cias.repository.CardRepository;
import com.CardMaster.modules.paa.repository.CardApplicationRepository;
import com.CardMaster.modules.cias.dto.CardRequestDto;
import com.CardMaster.modules.cau.entity.UnderwritingDecision;
import com.CardMaster.modules.cias.entity.Card;
import com.CardMaster.modules.paa.entity.CardApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardIssuanceService {

    private final CardRepository cardRepository;
    private final CardApplicationRepository applicationRepository;
    private final UnderwritingDecisionRepository decisionRepository;

    public Card createCard(CardRequestDto requestDto) {
        CardApplication application = applicationRepository.findById(requestDto.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + requestDto.getApplicationId()));

        if (application.getStatus() != CardApplication.CardApplicationStatus.Approved) {
            throw new IllegalStateException("Only APPROVED applications can be issued");
        }

        UnderwritingDecision latestDecision = decisionRepository
                .findTopByApplication_ApplicationIdOrderByDecisionDateDesc(application.getApplicationId())
                .orElseThrow(() -> new IllegalStateException("Approved application is missing an underwriting decision"));

        if (latestDecision.getDecision() != UnderwritingDecisionType.APPROVE || latestDecision.getApprovedLimit() == null
                || latestDecision.getApprovedLimit() <= 0) {
            throw new IllegalStateException("Card issuance requires an APPROVE decision with a positive approved limit");
        }

        if (cardRepository.existsByApplicationApplicationId(application.getApplicationId())) {
            throw new IllegalStateException("A card has already been issued for this application");
        }

        Card card = new Card();
        card.setApplication(application);
        card.setCustomer(application.getCustomer());
        card.setProduct(application.getProduct());
        card.setMaskedCardNumber(requestDto.getMaskedCardNumber());
        card.setExpiryDate(requestDto.getExpiryDate());
        card.setCvvHash(requestDto.getCvvHash());
        card.setStatus(CardStatus.ISSUED);
        return cardRepository.save(card);
    }

    public Card getCardById(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found with ID: " + cardId));
    }

    public Card blockCard(Long cardId) { // Changed parameter type to Long to match existing usage
        Card card = getCardById(cardId);
        card.setStatus(CardStatus.BLOCKED);
        return cardRepository.save(card);
    }

    public Card getCardByApplicationId(Long applicationId) {
        return cardRepository.findByApplicationApplicationId(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found for application ID: " + applicationId));
    }

    public List<Card> getCardsByEmail(String email) {
        return cardRepository.findByCustomerContactInfoEmail(email);
    }
}
