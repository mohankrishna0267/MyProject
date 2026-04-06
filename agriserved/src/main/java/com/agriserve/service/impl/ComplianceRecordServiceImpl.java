package com.agriserve.service.impl;

import com.agriserve.dto.compliance.ComplianceRecordRequest;
import com.agriserve.dto.compliance.ComplianceRecordResponse;
import com.agriserve.entity.ComplianceRecord;
import com.agriserve.repository.ComplianceRecordRepository;
import com.agriserve.service.ComplianceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplianceRecordServiceImpl implements ComplianceRecordService {

    private final ComplianceRecordRepository repository;

    @Override
    public ComplianceRecordResponse createRecord(ComplianceRecordRequest request) {

        ComplianceRecord record = ComplianceRecord.builder()
                .entityId(request.getEntityId())
                .entityType(request.getEntityType())
                .result(request.getResult())
                .complianceDate(request.getComplianceDate())
                .build();

        record = repository.save(record);

        return ComplianceRecordResponse.builder()
                .complianceId(record.getComplianceId())
                .result(record.getResult())
                .build();
    }

    @Override
    public List<ComplianceRecordResponse> getRecords(Long entityId, String entityType) {

        return repository.findByEntityIdAndEntityType(entityId, entityType)
                .stream()
                .map(r -> ComplianceRecordResponse.builder()
                        .complianceId(r.getComplianceId())
                        .result(r.getResult())
                        .build())
                .toList();
    }
}