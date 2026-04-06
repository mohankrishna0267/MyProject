package com.agriserve.repository;

import com.agriserve.entity.AdvisorySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvisorySessionRepository extends JpaRepository<AdvisorySession, Long> {
}