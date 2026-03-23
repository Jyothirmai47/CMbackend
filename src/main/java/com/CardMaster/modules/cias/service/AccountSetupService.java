package com.CardMaster.modules.cias.service;

import com.CardMaster.modules.cias.enums.AccountStatus;
import com.CardMaster.modules.cias.enums.CardStatus;
import com.CardMaster.modules.cau.enums.UnderwritingDecisionType;
import com.CardMaster.modules.cau.repository.UnderwritingDecisionRepository;
import com.CardMaster.modules.cias.repository.CardRepository;
import com.CardMaster.modules.cias.repository.CardAccountRepository;
import com.CardMaster.modules.cias.dto.CardAccountRequestDto;
import com.CardMaster.modules.cau.entity.UnderwritingDecision;
import com.CardMaster.modules.cias.entity.Card;
import com.CardMaster.modules.cias.entity.CardAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AccountSetupService {

    private final CardAccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final UnderwritingDecisionRepository decisionRepository;

    public CardAccount createAccount(CardAccountRequestDto requestDto) {
        Card card = cardRepository.findById(requestDto.getCardId())
                .orElseThrow(() -> new IllegalArgumentException("Card not found with ID: " + requestDto.getCardId()));

        if (card.getStatus() != CardStatus.ISSUED) {
            throw new IllegalStateException("Card must be ISSUED before linking to an account");
        }

        if (accountRepository.findByCardCardId(card.getCardId()).isPresent()) {
            throw new IllegalStateException("An account already exists for this card");
        }

        UnderwritingDecision latestDecision = decisionRepository
                .findTopByApplication_ApplicationIdOrderByDecisionDateDesc(card.getApplication().getApplicationId())
                .orElseThrow(() -> new IllegalStateException("No underwriting decision found for card application"));

        if (latestDecision.getDecision() != UnderwritingDecisionType.APPROVE || latestDecision.getApprovedLimit() == null
                || latestDecision.getApprovedLimit() <= 0) {
            throw new IllegalStateException("Account setup requires an APPROVE decision with a positive approved limit");
        }

        CardAccount account = new CardAccount();
        account.setCard(card);
        account.setCreditLimit(latestDecision.getApprovedLimit());
        account.setAvailableLimit(latestDecision.getApprovedLimit());
        account.setOpenDate(LocalDate.now()); // auto-set today's date
        account.setStatus(AccountStatus.ACTIVE);

        // activate card
        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);

        return accountRepository.save(account);
    }

    public CardAccount getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
    }

    public CardAccount getAccountByEmail(String email) {
        return accountRepository.findByCardCustomerContactInfoEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No account found for email: " + email));
    }

    public CardAccount useCard(Long accountId, Double amount) {
        CardAccount account = getAccountById(accountId);

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (account.getAvailableLimit() < amount) {
            throw new IllegalStateException("Insufficient available limit");
        }

        account.setAvailableLimit(account.getAvailableLimit() - amount);
        return accountRepository.save(account);
    }
}
