package com.agriserve.dto.request;

import com.agriserve.entity.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

/**
 * Request DTO for creating or updating a Farmer profile.
 */
@Data
public class FarmerRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String address;

    private String contactInfo;

    @Positive(message = "Land size must be positive")
    private Double landSize;

    private String cropType;
}
