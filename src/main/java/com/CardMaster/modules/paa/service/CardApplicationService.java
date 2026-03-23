
package com.CardMaster.modules.paa.service;

import com.CardMaster.modules.cpl.repository.CardProductRepository;
import com.CardMaster.modules.paa.repository.CardApplicationRepository;
import com.CardMaster.modules.paa.repository.CustomerRepository;
import com.CardMaster.modules.paa.dto.CardApplicationDto;
import com.CardMaster.modules.paa.exception.ApplicationNotFoundException;
import com.CardMaster.modules.paa.exception.CustomerNotFoundException;
import com.CardMaster.modules.cpl.exception.NotFoundException;
import com.CardMaster.modules.paa.exception.DuplicateApplicationException;
import com.CardMaster.modules.paa.mapper.EntityMapper;
import com.CardMaster.modules.cpl.entity.CardProduct;
import com.CardMaster.modules.paa.entity.CardApplication;
import com.CardMaster.modules.paa.entity.Customer;
import com.CardMaster.modules.iam.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class
CardApplicationService {

    private final CardApplicationRepository applicationRepository;
    private final CustomerRepository customerRepository;
    private final CardProductRepository productRepo;
    private final JwtUtil jwtUtil;


    // --- Create Application ---
    public CardApplicationDto create(CardApplicationDto dto, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + dto.getCustomerId()));


        List<CardApplication> existingApps = applicationRepository.findByCustomerCustomerId(dto.getCustomerId());
        boolean hasActive = existingApps.stream()
                .anyMatch(app -> app.getStatus() == CardApplication.CardApplicationStatus.Submitted
                        || app.getStatus() == CardApplication.CardApplicationStatus.UnderReview);
        if (hasActive) {
            throw new DuplicateApplicationException("Customer already has an active application. Only one allowed.");
        }

        CardProduct product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        CardApplication application = EntityMapper.toCardApplicationEntity(dto, customer, product);
        application.setStatus(CardApplication.CardApplicationStatus.Submitted);

        CardApplication saved = applicationRepository.save(application);
        return EntityMapper.toCardApplicationDto(saved);
    }

    // --- Get Application by ID ---
    public CardApplicationDto findById(Long id, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        CardApplication app = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found with id: " + id));
        return EntityMapper.toCardApplicationDto(app);
    }

    // --- Get All Applications ---
    public List<CardApplicationDto> getAllApplications(String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        List<CardApplication> apps = applicationRepository.findAll();
        List<CardApplicationDto> dtos = new ArrayList<>();
        for (CardApplication app : apps) {
            dtos.add(EntityMapper.toCardApplicationDto(app));
        }
        return dtos;
    }

    // --- Get Applications by Customer ---
    public List<CardApplicationDto> getApplicationsByCustomer(Long customerId, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        List<CardApplication> apps = applicationRepository.findByCustomerCustomerId(customerId);
        List<CardApplicationDto> dtos = new ArrayList<>();
        for (CardApplication app : apps) {
            dtos.add(EntityMapper.toCardApplicationDto(app));
        }
        return dtos;
    }


    public CardApplicationDto updateApplicationStatus(Long appId, String status, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        CardApplication app = applicationRepository.findById(appId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found with id: " + appId));

        if (app.getStatus() == CardApplication.CardApplicationStatus.Approved
                || app.getStatus() == CardApplication.CardApplicationStatus.Rejected) {
            throw new IllegalStateException("Final application status can only be set by underwriting");
        }

        String normalized = status.toUpperCase();
        if (!"UNDERREVIEW".equals(normalized)) {
            throw new IllegalArgumentException("Only UNDERREVIEW can be set via this endpoint");
        }

        app.setStatus(CardApplication.CardApplicationStatus.UnderReview);
        applicationRepository.save(app);

        return EntityMapper.toCardApplicationDto(app);
    }
}
