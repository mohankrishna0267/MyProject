package com.agriserve.entity;

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
    @Index(name = "idx_metric_program", columnList = "program_id")
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
    @JoinColumn(name = "program_id", nullable = false)
    private TrainingProgram program;

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
