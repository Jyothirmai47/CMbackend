package com.CardMaster.modules.paa.repository;

import com.CardMaster.modules.paa.entity.CardApplication;
import com.CardMaster.modules.paa.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardApplicationRepository extends JpaRepository<CardApplication, Long> {
    List<CardApplication> findByCustomerCustomerId(Long customerId);
}
