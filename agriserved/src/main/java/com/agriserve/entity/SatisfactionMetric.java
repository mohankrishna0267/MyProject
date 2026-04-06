package com.agriserve.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "satisfaction_metrics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SatisfactionMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long metricId;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private TrainingProgram program;

    @Column(nullable = false)
    private double score;

    @Column(nullable = false)
    private LocalDate metricDate;

    @Enumerated(EnumType.STRING)
    private MetricStatus status;

    public enum MetricStatus {
        ACTIVE, EXDELLENT, STABLE, NEEDS_IMPROVEMENT, INACTIVE
    }
}