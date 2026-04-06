package com.agriserve.dto.compliance;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditResponse {

    private Long auditId;
    private Long officerId;
    private String scope;
    private String findings;
    private LocalDate auditDate;
    private LocalDateTime createdAt;
}