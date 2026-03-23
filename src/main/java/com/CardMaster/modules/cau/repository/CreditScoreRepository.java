package com.CardMaster.modules.cau.repository;

import com.CardMaster.modules.cau.entity.CreditScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditScoreRepository extends JpaRepository<CreditScore, Long> {
    Optional<CreditScore> findTopByApplication_ApplicationIdOrderByGeneratedDateDesc(Long applicationId);
}