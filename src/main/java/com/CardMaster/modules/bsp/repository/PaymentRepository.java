package com.CardMaster.modules.bsp.repository;


import com.CardMaster.modules.bsp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByAccount_AccountIdOrderByPaymentDateDesc(Long accountId);
}
