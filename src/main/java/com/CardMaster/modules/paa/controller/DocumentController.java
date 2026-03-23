package com.CardMaster.modules.paa.controller;

import com.CardMaster.modules.paa.dto.DocumentDto;
import com.CardMaster.common.dto.ApiResponse;
import com.CardMaster.modules.paa.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private static final Logger log = LogManager.getLogger(DocumentController.class);
    private final DocumentService service;

    // --- Upload Document ---
    @PostMapping
    public ResponseEntity<ApiResponse<DocumentDto>> upload(
            @RequestBody DocumentDto dto,
            @RequestHeader("Authorization") String token) {

        log.info("Uploading document for application {}", dto.getApplicationId());
        DocumentDto saved = service.uploadDocument(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Document uploaded successfully", saved));
    }

    // --- Get Document by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentDto>> getById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        log.info("Fetching document {}", id);
        DocumentDto doc = service.getDocument(id, token);
        return ResponseEntity.ok(new ApiResponse<>("Document retrieved successfully", doc));
    }

    // --- Get Documents by Application ---
    @GetMapping("/application/{applicationId}")
    public ResponseEntity<ApiResponse<List<DocumentDto>>> getByApplication(
            @PathVariable Long applicationId,
            @RequestHeader("Authorization") String token) {

        log.info("Fetching documents for application {}", applicationId);
        List<DocumentDto> docs = service.getDocumentsByApplication(applicationId, token);
        return ResponseEntity.ok(new ApiResponse<>("Documents retrieved successfully", docs));
    }

    // --- Update Document Status ---
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<DocumentDto>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestHeader("Authorization") String token) {

        log.info("Updating document {} status to {}", id, status);
        DocumentDto updated = service.updateDocumentStatus(id, status, token);
        return ResponseEntity.ok(new ApiResponse<>("Document status updated successfully", updated));
    }

    // --- Delete Document ---
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        log.info("Deleting document {}", id);
        service.deleteDocument(id, token);
        return ResponseEntity.ok(new ApiResponse<>("Document deleted successfully", null));
    }

    // --- Get All Documents ---
    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentDto>>> getAllDocuments(
            @RequestHeader("Authorization") String token) {

        log.info("Fetching all documents");
        List<DocumentDto> docs = service.getAllDocuments(token);
        return ResponseEntity.ok(new ApiResponse<>("All documents retrieved successfully", docs));
    }
}
