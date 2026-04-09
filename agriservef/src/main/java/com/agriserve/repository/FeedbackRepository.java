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

    Page<Feedback> findAllByFarmer_FarmerId(Long farmerId, Pageable pageable);

    Page<Feedback> findAllBySession_SessionId(Long sessionId, Pageable pageable);

    Optional<Feedback> findByFarmer_FarmerIdAndSession_SessionId(Long farmerId, Long sessionId);

    /** Compute average rating for a specific advisory session */
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.session.sessionId = :sessionId")
    Double findAverageRatingBySessionId(@Param("sessionId") Long sessionId);

    /** Compute average rating for all sessions under a training program's workshops */
    @Query("SELECT AVG(f.rating) FROM Feedback f " +
           "JOIN f.session s JOIN s.farmer fa JOIN fa.participations p " +
           "JOIN p.workshop w WHERE w.program.programId = :programId")
    Double findAverageRatingByProgramId(@Param("programId") Long programId);
}
