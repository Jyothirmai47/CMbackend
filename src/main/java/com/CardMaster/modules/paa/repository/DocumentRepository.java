package com.CardMaster.modules.paa.repository;

import com.CardMaster.modules.paa.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByApplicationApplicationId(Long applicationId);
}
