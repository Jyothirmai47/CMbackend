package com.CardMaster.modules.paa.controller;

import com.CardMaster.modules.paa.dto.CardApplicationDto;
import com.CardMaster.common.dto.ApiResponse;
import com.CardMaster.modules.paa.service.CardApplicationService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class CardApplicationController {

    private static final Logger log = LogManager.getLogger(CardApplicationController.class);
    private final CardApplicationService applicationService;

    // --- Create Application ---
    @PostMapping
    public ResponseEntity<ApiResponse<CardApplicationDto>> create(
            @Valid @RequestBody CardApplicationDto dto,
            @RequestHeader("Authorization") String token) {

        log.info("Creating card application for customer {}", dto.getCustomerId());
        CardApplicationDto saved = applicationService.create(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Application created successfully", saved));
    }

    // --- Get Application by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CardApplicationDto>> getById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        log.info("Fetching application {}", id);
        CardApplicationDto dto = applicationService.findById(id, token);
        return ResponseEntity.ok(new ApiResponse<>("Application retrieved successfully", dto));
    }

    // --- Get All Applications ---
    @GetMapping
    public ResponseEntity<ApiResponse<List<CardApplicationDto>>> getAllApplications(
            @RequestHeader("Authorization") String token) {

        log.info("Fetching all applications");
        List<CardApplicationDto> apps = applicationService.getAllApplications(token);
        return ResponseEntity.ok(new ApiResponse<>("All applications retrieved successfully", apps));
    }

    // --- Get Applications by Customer ---
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<CardApplicationDto>>> getByCustomer(
            @PathVariable Long customerId,
            @RequestHeader("Authorization") String token) {

        log.info("Fetching applications for customer {}", customerId);
        List<CardApplicationDto> apps = applicationService.getApplicationsByCustomer(customerId, token);

        if (apps == null || apps.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Customer doesn't have any applications", null));
        }

        return ResponseEntity.ok(new ApiResponse<>("Applications retrieved successfully", apps));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Object>> getStatus(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        log.info("Fetching status for application {}", id);
        CardApplicationDto dto = applicationService.findById(id, token);

        // Build a small response object with just id + status
        Map<String, Object> statusResponse = new HashMap<>();
        statusResponse.put("applicationId", dto.getApplicationId());
        statusResponse.put("status", dto.getStatus());

        return ResponseEntity.ok(
                new ApiResponse<>("Application status retrieved successfully", statusResponse)
        );
    }

    // --- Update Application Status ---
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<CardApplicationDto>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestHeader("Authorization") String token) {

        log.info("Updating status for application {} to {}", id, status);
        CardApplicationDto updated = applicationService.updateApplicationStatus(id, status, token);
        return ResponseEntity.ok(new ApiResponse<>("Application status updated successfully", updated));
    }


}
