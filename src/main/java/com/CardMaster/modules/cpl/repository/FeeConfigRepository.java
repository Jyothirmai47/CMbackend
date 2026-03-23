package com.CardMaster.modules.cpl.repository;

import com.CardMaster.modules.cpl.entity.FeeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeeConfigRepository extends JpaRepository<FeeConfig, Long> {
    List<FeeConfig> findByProduct_ProductId(Long productId);
}