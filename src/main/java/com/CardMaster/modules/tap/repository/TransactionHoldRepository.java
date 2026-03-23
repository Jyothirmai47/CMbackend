package com.CardMaster.modules.tap.repository;

import com.CardMaster.modules.tap.entity.TransactionHold;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionHoldRepository extends JpaRepository<TransactionHold, Long> {
    List<TransactionHold> findByTransaction_TransactionId(Long transactionId);

    Optional<TransactionHold> findFirstByTransaction_TransactionIdAndReleaseDateIsNullOrderByHoldDateAsc(Long transactionId);
}
