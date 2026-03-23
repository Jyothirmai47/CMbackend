
package com.CardMaster.modules.cau.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditScoreGenerateRequest {
    @Positive(message = "Bureau score must be positive")
    private Integer bureauScore;


}