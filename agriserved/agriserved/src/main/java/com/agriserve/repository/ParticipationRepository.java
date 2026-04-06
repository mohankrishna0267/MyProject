package com.agriserve.repository;

import com.agriserve.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    @Query("SELECT AVG(p.rating) FROM Participation p " +
            "JOIN p.workshop w " +
            "WHERE w.program.programId = :programId")
    Double findAverageRatingByProgramId(Long programId);
}