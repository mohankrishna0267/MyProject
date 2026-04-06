package com.agriserve.repository;

import com.agriserve.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Check duplicate feedback (one per farmer-session)
    boolean existsByFarmer_FarmerIdAndSession_SessionId(Long farmerId, Long sessionId);

    // Get feedback by session
    List<Feedback> findBySession_SessionId(Long sessionId);

    // (Optional – if needed later)
    Optional<Feedback> findByFarmer_FarmerIdAndSession_SessionId(Long farmerId, Long sessionId);

    // Average rating for program (used in metric calculation)
    @org.springframework.data.jpa.repository.Query(
            "SELECT AVG(f.rating) FROM Feedback f " +
                    "JOIN f.session s " +
                    "JOIN s.farmer fa " +
                    "JOIN fa.participations p " +
                    "JOIN p.workshop w " +
                    "WHERE w.program.programId = :programId"
    )
    Optional<Double> findAverageRatingByProgramId(Long programId);
}