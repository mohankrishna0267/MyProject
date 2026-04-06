package com.agriserve.dto.compliance;

import com.agriserve.entity.ComplianceRecord;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ComplianceRecordRequest {

    @NotNull
    private Long entityId;

    @NotNull
    private ComplianceRecord.ComplianceType entityType;

    @NotNull
    private ComplianceRecord.ComplianceResult result;

    @NotNull
    private LocalDate complianceDate;

    private String notes;
}