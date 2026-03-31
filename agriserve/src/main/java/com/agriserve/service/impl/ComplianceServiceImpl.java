package com.agriserve.service.impl;

import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.compliance.AuditRequest;
import com.agriserve.dto.compliance.AuditResponse;
import com.agriserve.dto.compliance.ComplianceRecordRequest;
import com.agriserve.dto.compliance.ComplianceRecordResponse;
import com.agriserve.entity.Audit;
import com.agriserve.entity.ComplianceRecord;
import com.agriserve.entity.User;
import com.agriserve.exception.ResourceNotFoundException;
import com.agriserve.repository.AuditRepository;
import com.agriserve.repository.ComplianceRecordRepository;
import com.agriserve.repository.UserRepository;
import com.agriserve.service.ComplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComplianceServiceImpl implements ComplianceService {

    private final ComplianceRecordRepository recordRepository;
    private final AuditRepository auditRepository;
    private final UserRepository userRepository;

    @Override
    public ComplianceRecordResponse createComplianceRecord(ComplianceRecordRequest request) {
        ComplianceRecord record = ComplianceRecord.builder()
                .entityId(request.getEntityId())
                .entityType(request.getEntityType())
                .type(request.getType())
                .result(request.getResult())
                .complianceDate(request.getComplianceDate())
                .notes(request.getNotes())
                .build();

        record = recordRepository.save(record);
        return mapToRecordResponse(record);
    }

    @Override
    public PagedResponse<ComplianceRecordResponse> getRecordsByEntity(Long entityId, String entityType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ComplianceRecord> pageResult = recordRepository.findByEntityIdAndEntityType(entityId, entityType, pageable);
        return PagedResponse.from(pageResult.map(this::mapToRecordResponse));
    }

    @Override
    public AuditResponse createAudit(AuditRequest request) {
        User officer = userRepository.findById(request.getOfficerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getOfficerId()));

        Audit audit = Audit.builder()
                .officer(officer)
                .scope(request.getScope())
                .findings(request.getFindings())
                .auditDate(request.getAuditDate())
                .status(request.getStatus())
                .build();

        audit = auditRepository.save(audit);
        return mapToAuditResponse(audit);
    }

    @Override
    public AuditResponse getAuditById(Long auditId) {
        Audit audit = auditRepository.findById(auditId)
                .orElseThrow(() -> new ResourceNotFoundException("Audit", "id", auditId));
        return mapToAuditResponse(audit);
    }

    @Override
    public PagedResponse<AuditResponse> getAllAudits(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Audit> auditPage = auditRepository.findAll(pageable);
        return PagedResponse.from(auditPage.map(this::mapToAuditResponse));
    }

    private ComplianceRecordResponse mapToRecordResponse(ComplianceRecord record) {
        return ComplianceRecordResponse.builder()
                .complianceId(record.getComplianceId())
                .entityId(record.getEntityId())
                .entityType(record.getEntityType())
                .type(record.getType())
                .result(record.getResult())
                .complianceDate(record.getComplianceDate())
                .notes(record.getNotes())
                .createdAt(record.getCreatedAt())
                .build();
    }

    private AuditResponse mapToAuditResponse(Audit audit) {
        return AuditResponse.builder()
                .auditId(audit.getAuditId())
                .officerId(audit.getOfficer().getUserId())
                .officerName(audit.getOfficer().getName())
                .scope(audit.getScope())
                .findings(audit.getFindings())
                .auditDate(audit.getAuditDate())
                .status(audit.getStatus())
                .createdAt(audit.getCreatedAt())
                .build();
    }
}
