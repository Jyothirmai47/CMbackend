package com.CardMaster.modules.cau.repository;

import com.CardMaster.modules.cau.entity.UnderwritingDecision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnderwritingDecisionRepository extends JpaRepository<UnderwritingDecision, Long> {

    Optional<UnderwritingDecision>
    findTopByApplication_ApplicationIdOrderByDecisionDateDesc(Long applicationId);
}
