package com.CardMaster.modules.paa.repository;

import com.CardMaster.modules.paa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByContactInfoEmail(String email);
}
