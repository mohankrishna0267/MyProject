package com.agriserve.controller;

import com.agriserve.dto.ApiResponse;
import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.compliance.AuditRequest;
import com.agriserve.dto.compliance.AuditResponse;
import com.agriserve.dto.compliance.ComplianceRecordRequest;
import com.agriserve.dto.compliance.ComplianceRecordResponse;
import com.agriserve.service.ComplianceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compliance")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('COMPLIANCE_OFFICER', 'GOVERNMENT_AUDITOR', 'ADMIN')")
public class ComplianceController {

    private final ComplianceService complianceService;

    // --- Records ---
    
    @PostMapping("/records")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ComplianceRecordResponse> createRecord(@Valid @RequestBody ComplianceRecordRequest request) {
        return ApiResponse.success("Compliance record created", complianceService.createComplianceRecord(request));
    }

    @GetMapping("/records/{entityType}/{entityId}")
    public ApiResponse<PagedResponse<ComplianceRecordResponse>> getRecordsByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(complianceService.getRecordsByEntity(entityId, entityType, page, size));
    }

    // --- Audits ---

    @PostMapping("/audits")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuditResponse> createAudit(@Valid @RequestBody AuditRequest request) {
        return ApiResponse.success("Audit created", complianceService.createAudit(request));
    }

    @GetMapping("/audits")
    public ApiResponse<PagedResponse<AuditResponse>> getAllAudits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(complianceService.getAllAudits(page, size));
    }
}
