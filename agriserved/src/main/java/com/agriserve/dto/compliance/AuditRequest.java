package com.agriserve.dto.compliance;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AuditRequest {

    @NotNull
    private Long officerId;

    @NotBlank
    private String scope;

    private String findings;

    @NotNull
    private LocalDate auditDate;
}