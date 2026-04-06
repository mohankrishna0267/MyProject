package com.agriserve.service;

import com.agriserve.dto.compliance.ComplianceRecordRequest;
import com.agriserve.dto.compliance.ComplianceRecordResponse;

import java.util.List;

public interface ComplianceRecordService {

    ComplianceRecordResponse createRecord(ComplianceRecordRequest request);

    List<ComplianceRecordResponse> getRecords(Long entityId, String entityType);
}