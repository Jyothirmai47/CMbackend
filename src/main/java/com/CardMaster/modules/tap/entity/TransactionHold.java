package com.CardMaster.modules.tap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;


@Entity
@Table(name = "transaction_holds")
@Data
public class TransactionHold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long holdId;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @NotNull @Positive
    private Double amount;

    @NotNull
    private LocalDateTime holdDate;

    private LocalDateTime releaseDate;
}
