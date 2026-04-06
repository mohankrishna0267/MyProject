package com.agriserve.repository;

import com.agriserve.entity.SatisfactionMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SatisfactionMetricRepository extends JpaRepository<SatisfactionMetric, Long> {

    // Get metrics by program
    List<SatisfactionMetric> findByProgram_ProgramId(Long programId);
}