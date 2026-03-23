package com.CardMaster.modules.bsp.repository;

import com.CardMaster.modules.bsp.enums.StatementStatus;
import com.CardMaster.modules.bsp.entity.Statement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatementRepository extends JpaRepository<Statement, Long> {
    List<Statement> findByAccount_AccountIdOrderByGeneratedDateDesc(Long accountId);

    Optional<Statement> findFirstByAccount_AccountIdAndStatusOrderByGeneratedDateDesc(
            Long accountId,
            StatementStatus status
    );
}
