package com.agriserve.dto.compliance;

import com.agriserve.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/** Request DTO for creating an audit record. */
@Data
public class AuditRequest {

    @NotNull(message = "Officer ID is required")
    private Long officerId;

    @NotBlank(message = "Audit scope is required")
    private String scope;

    private String findings;

    @NotNull(message = "Audit date is required")
    private LocalDate auditDate;

    private Status status = Status.DRAFT;
}
