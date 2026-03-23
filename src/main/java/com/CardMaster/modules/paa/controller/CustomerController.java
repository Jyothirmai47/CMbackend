package com.CardMaster.modules.paa.controller;

import com.CardMaster.modules.paa.dto.CustomerDto;
import com.CardMaster.modules.paa.service.CustomerService;
import com.CardMaster.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    // --- Create Customer ---
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDto>> createCustomer(
            @Valid @RequestBody CustomerDto dto,
            @RequestHeader("Authorization") String token) {

        CustomerDto created = service.createCustomer(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Customer created successfully", created));
    }

    // --- Get Customer by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomer(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        CustomerDto customer = service.getCustomer(id, token);
        return ResponseEntity.ok(new ApiResponse<>("Customer retrieved successfully", customer));
    }

    // --- Update Customer ---
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDto dto,
            @RequestHeader("Authorization") String token) {

        CustomerDto updated = service.updateCustomer(id, dto, token);
        return ResponseEntity.ok(new ApiResponse<>("Customer updated successfully", updated));
    }

    // --- Delete Customer ---
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        service.deleteCustomer(id, token);
        return ResponseEntity.ok(new ApiResponse<>("Customer deleted successfully", null));
    }

    // --- Get All Customers ---
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getAllCustomers(
            @RequestHeader("Authorization") String token) {

        List<CustomerDto> customers = service.getAllCustomers(token);
        return ResponseEntity.ok(new ApiResponse<>("Customers retrieved successfully", customers));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<CustomerDto>> getMyCustomer(
            Principal principal,
            @RequestHeader("Authorization") String token) {

        CustomerDto customer = service.getCustomerByEmail(principal.getName(), token);
        return ResponseEntity.ok(new ApiResponse<>("Customer profile retrieved successfully", customer));
    }

    @PutMapping("/my")
    public ResponseEntity<ApiResponse<CustomerDto>> updateMyCustomer(
            Principal principal,
            @Valid @RequestBody CustomerDto dto,
            @RequestHeader("Authorization") String token) {

        CustomerDto updated = service.updateCustomerByEmail(principal.getName(), dto, token);
        return ResponseEntity.ok(new ApiResponse<>("Customer profile updated successfully", updated));
    }
}
