package com.agriserve.dto.compliance;

import com.agriserve.enums.Status;
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
public class AuditResponse {

    private Long auditId;
    private Long officerId;
    private String officerName;
    private String scope;
    private String findings;
    private LocalDate auditDate;
    private Status status;
    private LocalDateTime createdAt;
}
