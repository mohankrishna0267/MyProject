package com.agriserve.service.impl;

import com.agriserve.dto.request.AuditRequest;
import com.agriserve.dto.request.ComplianceRecordRequest;
import com.agriserve.dto.response.AuditResponse;
import com.agriserve.dto.response.ComplianceRecordResponse;
import com.agriserve.entity.Audit;
import com.agriserve.entity.ComplianceRecord;
import com.agriserve.entity.User;
import com.agriserve.entity.enums.Status;
import com.agriserve.exception.ResourceNotFoundException;
import com.agriserve.repository.AuditRepository;
import com.agriserve.repository.ComplianceRecordRepository;
import com.agriserve.repository.UserRepository;
import com.agriserve.service.ComplianceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manages compliance records and formal audits.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ComplianceServiceImpl implements ComplianceService {

    private final ComplianceRecordRepository complianceRecordRepository;
    private final AuditRepository auditRepository;
    private final UserRepository userRepository;

    // ─── Compliance Records ───────────────────────────────────────────────────

    @Override
    @Transactional
    public ComplianceRecordResponse createComplianceRecord(ComplianceRecordRequest request) {
        ComplianceRecord record = ComplianceRecord.builder()
                .entityId(request.getEntityId())
                .complianceType(request.getComplianceType())
                .result(request.getResult())
                .notes(request.getNotes())
                .status(Status.ACTIVE)
                .build();
        return ComplianceRecordResponse.from(complianceRecordRepository.save(record));
    }

    @Override
    @Transactional(readOnly = true)
    public ComplianceRecordResponse getComplianceRecordById(Long complianceId) {
        return ComplianceRecordResponse.from(
                complianceRecordRepository.findById(complianceId)
                        .orElseThrow(() -> new ResourceNotFoundException("ComplianceRecord", "id", complianceId))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplianceRecordResponse> getComplianceByEntityId(Long entityId, Pageable pageable) {
        return complianceRecordRepository.findAllByEntityId(entityId, pageable)
                .map(ComplianceRecordResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplianceRecordResponse> getAllComplianceRecords(Pageable pageable) {
        return complianceRecordRepository.findAll(pageable).map(ComplianceRecordResponse::from);
    }

    // ─── Audits ───────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public AuditResponse createAudit(AuditRequest request) {
        User officer = userRepository.findById(request.getOfficerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getOfficerId()));

        Audit audit = Audit.builder()
                .officer(officer)
                .scope(request.getScope())
                .findings(request.getFindings())
                .status(Status.PENDING)
                .build();
        return AuditResponse.from(auditRepository.save(audit));
    }

    @Override
    @Transactional(readOnly = true)
    public AuditResponse getAuditById(Long auditId) {
        return AuditResponse.from(
                auditRepository.findById(auditId)
                        .orElseThrow(() -> new ResourceNotFoundException("Audit", "id", auditId))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditResponse> getAllAudits(Status status, Pageable pageable) {
        if (status != null) {
            return auditRepository.findAllByStatus(status, pageable).map(AuditResponse::from);
        }
        return auditRepository.findAll(pageable).map(AuditResponse::from);
    }

    @Override
    @Transactional
    public AuditResponse updateAuditStatus(Long auditId, Status status) {
        Audit audit = auditRepository.findById(auditId)
                .orElseThrow(() -> new ResourceNotFoundException("Audit", "id", auditId));
        audit.setStatus(status);
        return AuditResponse.from(auditRepository.save(audit));
    }
}
