package com.agriserve.service.impl;

import com.agriserve.dto.compliance.AuditRequest;
import com.agriserve.dto.compliance.AuditResponse;
import com.agriserve.entity.Audit;
import com.agriserve.entity.User;
import com.agriserve.repository.AuditRepository;
import com.agriserve.repository.UserRepository;
import com.agriserve.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final UserRepository userRepository;

    @Override
    public AuditResponse createAudit(AuditRequest request) {

        User officer = userRepository.findById(request.getOfficerId()).orElseThrow();

        Audit audit = Audit.builder()
                .officer(officer)
                .scope(request.getScope())
                .findings(request.getFindings())
                .auditDate(request.getAuditDate())
                .build();

        audit = auditRepository.save(audit);

        return AuditResponse.builder()
                .auditId(audit.getAuditId())
                .scope(audit.getScope())
                .build();
    }

    @Override
    public List<AuditResponse> getAllAudits() {

        return auditRepository.findAll()
                .stream()
                .map(a -> AuditResponse.builder()
                        .auditId(a.getAuditId())
                        .scope(a.getScope())
                        .build())
                .toList();
    }
}
