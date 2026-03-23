package com.CardMaster.modules.bsp.service;

import com.CardMaster.modules.bsp.enums.PaymentStatus;
import com.CardMaster.modules.bsp.enums.StatementStatus;
import com.CardMaster.modules.cias.enums.AccountStatus;
import com.CardMaster.modules.bsp.repository.PaymentRepository;
import com.CardMaster.modules.bsp.repository.StatementRepository;
import com.CardMaster.modules.cias.repository.CardAccountRepository;
import com.CardMaster.modules.bsp.exception.PaymentFailedException;
import com.CardMaster.modules.bsp.entity.Payment;
import com.CardMaster.modules.bsp.entity.Statement;
import com.CardMaster.modules.cias.entity.CardAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final StatementRepository statementRepository;
    private final CardAccountRepository accountRepository;

    @Transactional
    public Payment capturePayment(Payment payment) {
        Long accountId = payment.getAccount().getAccountId();
        CardAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new PaymentFailedException("Account not found with id: " + accountId));

        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            throw new PaymentFailedException("Payment amount must be positive");
        }
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new PaymentFailedException("Payments can be captured only for ACTIVE accounts");
        }

        payment.setAccount(account);
        payment.setPaymentDate(payment.getPaymentDate() == null ? LocalDateTime.now() : payment.getPaymentDate());
        payment.setStatus(payment.getStatus() == null ? PaymentStatus.COMPLETED : payment.getStatus());

        Statement statement = resolveStatement(payment, accountId);
        payment.setStatement(statement);

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            return paymentRepository.save(payment);
        }

        account.setAvailableLimit(Math.min(account.getCreditLimit(), account.getAvailableLimit() + payment.getAmount()));
        accountRepository.save(account);

        if (statement != null) {
            applyPaymentToStatement(statement, payment.getAmount());
        }

        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Payment getById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentFailedException("Payment not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Payment> listAll() {
        return paymentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Payment> listByEmail(String email) {
        CardAccount account = accountRepository.findByCardCustomerContactInfoEmail(email)
                .orElseThrow(() -> new PaymentFailedException("No account found for email: " + email));
        return paymentRepository.findByAccount_AccountIdOrderByPaymentDateDesc(account.getAccountId());
    }

    private Statement resolveStatement(Payment payment, Long accountId) {
        Statement attachedStatement = payment.getStatement();
        if (attachedStatement != null) {
            Statement statement = statementRepository.findById(attachedStatement.getStatementId())
                    .orElseThrow(() -> new PaymentFailedException("Statement not found with id: " + attachedStatement.getStatementId()));

            if (!statement.getAccount().getAccountId().equals(accountId)) {
                throw new PaymentFailedException("Payment statement does not belong to the provided account");
            }
            if (statement.getStatus() != StatementStatus.OPEN) {
                throw new PaymentFailedException("Payments can only be applied to OPEN statements");
            }
            return statement;
        }

        return statementRepository
                .findFirstByAccount_AccountIdAndStatusOrderByGeneratedDateDesc(accountId, StatementStatus.OPEN)
                .orElse(null);
    }

    private void applyPaymentToStatement(Statement statement, double paymentAmount) {
        double newTotalDue = Math.max(0.0, statement.getTotalDue() - paymentAmount);
        statement.setTotalDue(newTotalDue);
        statement.setMinimumDue(Math.min(newTotalDue, statement.getMinimumDue()));

        if (newTotalDue == 0.0) {
            statement.setStatus(StatementStatus.CLOSED);
            statement.setMinimumDue(0.0);
        }

        statementRepository.save(statement);
    }
}
