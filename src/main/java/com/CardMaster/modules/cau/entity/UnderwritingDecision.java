package com.CardMaster.modules.cau.entity;

import com.CardMaster.modules.iam.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import com.CardMaster.modules.paa.entity.CardApplication;
import com.CardMaster.modules.cau.enums.UnderwritingDecisionType;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "underwriting_decision")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnderwritingDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long decisionId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private CardApplication application;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "underwriter_id", nullable = false)
    private User underwriter;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UnderwritingDecisionType decision;
    @PositiveOrZero
    private Double approvedLimit;
    private String remarks;
    @NotNull
    private LocalDateTime decisionDate;
}
