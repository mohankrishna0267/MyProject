package com.agriserve.dto.farmer;

import com.agriserve.enums.Gender;
import com.agriserve.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Response DTO for farmer profile data (safe to expose via API). */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarmerResponse {

    private Long farmerId;
    private String name;
    private LocalDate dob;
    private Gender gender;
    private String address;
    private String contactInfo;
    private BigDecimal landSize;
    private String cropType;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
