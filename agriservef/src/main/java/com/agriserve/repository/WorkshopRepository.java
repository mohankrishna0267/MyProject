package com.agriserve.repository;

import com.agriserve.entity.Workshop;
import com.agriserve.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Long> {

    Page<Workshop> findAllByProgram_ProgramId(Long programId, Pageable pageable);

    Page<Workshop> findAllByStatus(Status status, Pageable pageable);

    Page<Workshop> findAllByOfficer_UserId(Long officerId, Pageable pageable);
}
