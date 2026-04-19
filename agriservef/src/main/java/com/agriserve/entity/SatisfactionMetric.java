package com.agriserve.entity;

import com.agriserve.entity.enums.MetricType;
import com.agriserve.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Aggregated satisfaction metric computed for a training program.
 * Multiple metrics can exist per program (e.g., computed at different points in time).
 */
@Entity
@Table(name = "satisfaction_metrics", indexes = {
    @Index(name = "idx_metric_program", columnList = "program_id"),
    @Index(name = "idx_metric_officer", columnList = "officer_id"),
    @Index(name = "idx_metric_type", columnList = "metric_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SatisfactionMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_id")
    private Long metricId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private TrainingProgram program;

    /**
     * The Extension Officer this metric is computed for.
     * Populated only for OFFICER_PERFORMANCE metrics.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officer_id")
    private User officer;

    /**
     * Distinguishes between program satisfaction (Training → Participation.workshopRating)
     * and officer performance (Advisory → Feedback.rating).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type", nullable = false, length = 30)
    private MetricType metricType;

    /** Average satisfaction score out of 5 */
    @Column(nullable = false)
    private Double score;

    @CreationTimestamp
    @Column(name = "computed_date", updatable = false)
    private LocalDateTime computedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Status status = Status.ACTIVE;
}
