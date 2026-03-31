package com.agriserve.dto.farmer;

import com.agriserve.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Request DTO for creating or updating a farmer profile. */
@Data
public class FarmerRequest {

    @NotBlank(message = "Farmer name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    private LocalDate dob;

    private Gender gender;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @NotBlank(message = "Contact info is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Must be a valid 10-digit Indian phone number")
    private String contactInfo;

    @DecimalMin(value = "0.01", message = "Land size must be positive")
    @DecimalMax(value = "99999.99", message = "Land size exceeds limit")
    private BigDecimal landSize;

    @Size(max = 255, message = "Crop type info must not exceed 255 characters")
    private String cropType;
}
