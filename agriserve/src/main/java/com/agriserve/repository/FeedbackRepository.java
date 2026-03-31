package com.agriserve.repository;

import com.agriserve.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Optional<Feedback> findByFarmer_FarmerIdAndSession_SessionId(Long farmerId, Long sessionId);

    boolean existsByFarmer_FarmerIdAndSession_SessionId(Long farmerId, Long sessionId);

    Page<Feedback> findByFarmer_FarmerId(Long farmerId, Pageable pageable);

    Page<Feedback> findBySession_SessionId(Long sessionId, Pageable pageable);

    /** Average rating per session — used for satisfaction metric calculation. */
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.session.sessionId = :sessionId")
    Optional<Double> findAverageRatingBySessionId(@Param("sessionId") Long sessionId);

    /** Average rating per training program. */
    @Query("SELECT AVG(f.rating) FROM Feedback f " +
           "JOIN f.session s JOIN s.content c " +
           "WHERE s.sessionId IN " +
           "  (SELECT s2.sessionId FROM AdvisorySession s2 " +
           "   JOIN s2.farmer fa " +
           "   JOIN fa.participations p2 " +
           "   JOIN p2.workshop w2 " +
           "   WHERE w2.program.programId = :programId)")
    Optional<Double> findAverageRatingByProgramId(@Param("programId") Long programId);
}
