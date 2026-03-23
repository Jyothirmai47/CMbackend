package com.CardMaster.modules.cpl.repository;

import com.CardMaster.modules.cpl.entity.CardProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardProductRepository extends JpaRepository<CardProduct, Long> {}
