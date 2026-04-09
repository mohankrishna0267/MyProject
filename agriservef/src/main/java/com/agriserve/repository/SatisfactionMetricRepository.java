package com.agriserve.repository;

import com.agriserve.entity.SatisfactionMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SatisfactionMetricRepository extends JpaRepository<SatisfactionMetric, Long> {

    Page<SatisfactionMetric> findAllByProgram_ProgramId(Long programId, Pageable pageable);
}
