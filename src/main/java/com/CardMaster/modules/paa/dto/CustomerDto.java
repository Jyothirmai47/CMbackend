package com.CardMaster.modules.paa.dto;


import com.CardMaster.modules.paa.entity.ContactInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerDto {

    private Long customerId;
    @NotBlank
    private String name;

    @NotNull
    private String dob; // Represented as String (e.g., "1990-05-10") for JSON compatibility

    private ContactInfo contactInfo;

    @NotNull
    private Double income;

    @NotBlank
    private String employmentType; // Enum mapped as String

    @NotBlank
    private String status; // Enum mapped as String
}



