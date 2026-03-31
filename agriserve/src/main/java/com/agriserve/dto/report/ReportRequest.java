package com.agriserve.dto.report;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** Request DTO for generating reports. */
@Data
public class ReportRequest {

    @NotBlank(message = "Scope is required")
    private String scope;

    private String metrics; // JSON string payload
}
