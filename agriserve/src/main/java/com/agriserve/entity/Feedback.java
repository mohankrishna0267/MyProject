package com.agriserve.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Farmer feedback for a specific advisory session.
 * Unique constraint: one feedback per farmer-session pair.
 * Rating is strictly between 1 and 5 (Hibernate Validator enforced).
 */
@Entity
@Table(
    name = "feedbacks",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_feedback_farmer_session",
            columnNames = {"farmer_id", "session_id"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farmer_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_feedback_farmer"))
    private Farmer farmer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_feedback_session"))
    private AdvisorySession session;

    /**
     * Rating between 1 and 5. Validated by Hibernate Validator and enforced
     * at DB level via a check constraint added in the DDL.
     */
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
