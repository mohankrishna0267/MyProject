package com.agriserve.service;

import com.agriserve.dto.compliance.AuditRequest;
import com.agriserve.dto.compliance.AuditResponse;

import java.util.List;

public interface AuditService {

    AuditResponse createAudit(AuditRequest request);

    List<AuditResponse> getAllAudits();
}