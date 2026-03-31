package com.agriserve.service;

import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.compliance.AuditRequest;
import com.agriserve.dto.compliance.AuditResponse;
import com.agriserve.dto.compliance.ComplianceRecordRequest;
import com.agriserve.dto.compliance.ComplianceRecordResponse;

public interface ComplianceService {

    ComplianceRecordResponse createComplianceRecord(ComplianceRecordRequest request);

    PagedResponse<ComplianceRecordResponse> getRecordsByEntity(Long entityId, String entityType, int page, int size);

    AuditResponse createAudit(AuditRequest request);

    AuditResponse getAuditById(Long auditId);

    PagedResponse<AuditResponse> getAllAudits(int page, int size);
}
