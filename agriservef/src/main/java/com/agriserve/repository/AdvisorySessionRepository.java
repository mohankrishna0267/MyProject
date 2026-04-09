package com.agriserve.repository;

import com.agriserve.entity.AdvisorySession;
import com.agriserve.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvisorySessionRepository extends JpaRepository<AdvisorySession, Long> {

    Page<AdvisorySession> findAllByFarmer_FarmerId(Long farmerId, Pageable pageable);

    Page<AdvisorySession> findAllByOfficer_UserId(Long officerId, Pageable pageable);

    Page<AdvisorySession> findAllByStatus(Status status, Pageable pageable);
}
