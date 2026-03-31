package com.agriserve.dto.compliance;

import com.agriserve.enums.ComplianceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/** Request DTO for creating a compliance record. */
@Data
public class ComplianceRecordRequest {

    @NotNull(message = "Entity ID is required")
    private Long entityId;

    @NotBlank(message = "Entity type is required")
    private String entityType;

    @NotNull(message = "Compliance type is required")
    private ComplianceType type;

    @NotBlank(message = "Result is required")
    private String result;

    @NotNull(message = "Compliance date is required")
    private LocalDate complianceDate;

    private String notes;
}
