package com.agriserve.dto.compliance;

import com.agriserve.entity.ComplianceRecord;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceRecordResponse {

    private Long complianceId;
    private Long entityId;
    private ComplianceRecord.ComplianceType entityType;
    private ComplianceRecord.ComplianceResult result;
    private LocalDate complianceDate;
    private LocalDateTime createdAt;
}