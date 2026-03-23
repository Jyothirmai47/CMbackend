package com.CardMaster.modules.tap.entity;

import com.CardMaster.modules.tap.enums.TransactionChannel;
import com.CardMaster.modules.tap.enums.TransactionStatus;
import com.CardMaster.modules.cias.entity.CardAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private CardAccount account;


    @NotNull @Positive
    private Double amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String merchant;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionChannel channel;

    @NotNull
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionStatus status;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionHold> holds;
}
