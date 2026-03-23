package com.CardMaster.modules.paa.service;

import com.CardMaster.modules.paa.repository.CustomerRepository;
import com.CardMaster.modules.paa.dto.CustomerDto;
import com.CardMaster.modules.paa.exception.CustomerNotFoundException;
import com.CardMaster.modules.paa.mapper.EntityMapper;
import com.CardMaster.modules.paa.entity.ContactInfo;
import com.CardMaster.modules.paa.entity.Customer;
import com.CardMaster.modules.iam.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;

    // --- Create Customer ---
    public CustomerDto createCustomer(CustomerDto dto, String token) {
        String username = jwtUtil.extractUsername(token.substring(7));

        if (customerRepository.findByContactInfoEmail(username).isPresent()) {
            throw new IllegalStateException("A customer profile already exists for the logged-in email.");
        }

        ContactInfo contactInfo = dto.getContactInfo() == null
                ? new ContactInfo()
                : dto.getContactInfo();
        contactInfo.setEmail(username);
        dto.setContactInfo(contactInfo);

        Customer entity = EntityMapper.toCustomerEntity(dto);
        Customer saved = customerRepository.save(entity);
        return EntityMapper.toCustomerDto(saved);
    }

    // --- Get Customer by ID ---
    public CustomerDto getCustomer(Long id, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return EntityMapper.toCustomerDto(customer);
    }

    // --- Update Customer ---
    public CustomerDto updateCustomer(Long id, CustomerDto dto, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDob(java.time.LocalDate.parse(dto.getDob()));
        existing.setContactInfo(dto.getContactInfo());
        existing.setIncome(dto.getIncome());
        existing.setEmploymentType(Customer.EmploymentType.valueOf(dto.getEmploymentType()));
        existing.setStatus(Customer.CustomerStatus.valueOf(dto.getStatus()));

        Customer updated = customerRepository.save(existing);
        return EntityMapper.toCustomerDto(updated);
    }

    // --- Delete Customer ---
    public void deleteCustomer(Long id, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    // --- Get Customer by Email ---
    public CustomerDto getCustomerByEmail(String email, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        Customer customer = customerRepository.findByContactInfoEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
        return EntityMapper.toCustomerDto(customer);
    }

    // --- Get All Customers ---
    public List<CustomerDto> getAllCustomers(String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        List<Customer> customers = customerRepository.findAll();
        List<CustomerDto> dtos = new ArrayList<>();
        for (Customer customer : customers) {
            dtos.add(EntityMapper.toCustomerDto(customer));
        }
        return dtos;
    }

    public CustomerDto updateCustomerByEmail(String email, CustomerDto dto, String token) {
        jwtUtil.extractUsername(token.substring(7)); // validate token

        Customer existing = customerRepository.findByContactInfoEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));

        existing.setName(dto.getName());
        existing.setDob(java.time.LocalDate.parse(dto.getDob()));
        existing.setIncome(dto.getIncome());
        existing.setEmploymentType(Customer.EmploymentType.valueOf(dto.getEmploymentType()));
        // Note: We don't allow status update via this endpoint for now as it's a self-service update

        Customer updated = customerRepository.save(existing);
        return EntityMapper.toCustomerDto(updated);
    }
}
