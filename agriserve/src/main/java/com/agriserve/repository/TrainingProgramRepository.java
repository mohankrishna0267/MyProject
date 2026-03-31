package com.agriserve.repository;

import com.agriserve.entity.TrainingProgram;
import com.agriserve.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {

    Page<TrainingProgram> findByStatus(Status status, Pageable pageable);

    Page<TrainingProgram> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
