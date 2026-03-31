package com.agriserve.repository;

import com.agriserve.entity.AdvisorySession;
import com.agriserve.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvisorySessionRepository extends JpaRepository<AdvisorySession, Long> {

    Page<AdvisorySession> findByFarmer_FarmerId(Long farmerId, Pageable pageable);

    Page<AdvisorySession> findByOfficer_UserId(Long officerId, Pageable pageable);

    Page<AdvisorySession> findByStatus(Status status, Pageable pageable);

    Page<AdvisorySession> findByFarmer_FarmerIdAndStatus(Long farmerId, Status status, Pageable pageable);
}
