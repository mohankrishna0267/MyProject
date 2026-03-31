package com.agriserve.dto.compliance;

import com.agriserve.enums.ComplianceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceRecordResponse {

    private Long complianceId;
    private Long entityId;
    private String entityType;
    private ComplianceType type;
    private String result;
    private LocalDate complianceDate;
    private String notes;
    private LocalDateTime createdAt;
}
