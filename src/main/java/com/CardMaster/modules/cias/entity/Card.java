package com.CardMaster.modules.cias.entity;

import com.CardMaster.modules.cias.enums.CardStatus;
import com.CardMaster.modules.paa.entity.Customer;
import com.CardMaster.modules.paa.entity.CardApplication;
import com.CardMaster.modules.cpl.entity.CardProduct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Entity
@Table(name = "cards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long cardId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private CardProduct product;

    @OneToOne(optional = false)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private CardApplication application;

    @NotBlank
    @Column(name = "masked_card_number", nullable = false, length = 20, unique = true)
    private String maskedCardNumber;

    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @NotBlank
    @Column(name = "cvv_hash", nullable = false, unique = true)
    private String cvvHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;  // ISSUED, ACTIVE, BLOCKED

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL)
    private CardAccount cardAccount; // bidirectional mapping
}
