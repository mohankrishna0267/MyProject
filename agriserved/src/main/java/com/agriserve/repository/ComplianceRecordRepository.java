package com.agriserve.repository;

import com.agriserve.entity.ComplianceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplianceRecordRepository extends JpaRepository<ComplianceRecord, Long> {

    // Get records by entity (farmer, program, etc.)
    List<ComplianceRecord> findByEntityIdAndEntityType(Long entityId, String entityType);
}