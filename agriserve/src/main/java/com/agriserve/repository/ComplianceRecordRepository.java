package com.agriserve.repository;

import com.agriserve.entity.ComplianceRecord;
import com.agriserve.enums.ComplianceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplianceRecordRepository extends JpaRepository<ComplianceRecord, Long> {

    Page<ComplianceRecord> findByEntityIdAndEntityType(Long entityId, String entityType, Pageable pageable);

    Page<ComplianceRecord> findByType(ComplianceType type, Pageable pageable);

    Page<ComplianceRecord> findByResult(String result, Pageable pageable);
}
