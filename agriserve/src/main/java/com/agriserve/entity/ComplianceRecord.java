package com.agriserve.entity;

import com.agriserve.enums.ComplianceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A compliance record tracks whether a specific entity (farmer, program, etc.)
 * meets regulatory or procedural requirements.
 */
@Entity
@Table(name = "compliance_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplianceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compliance_id")
    private Long complianceId;

    /** ID of the entity under review (farmer, program, workshop, etc.). */
    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    /** Descriptive name of the entity type, e.g., "FARMER", "WORKSHOP". */
    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ComplianceType type;

    /** Pass/Fail/Partial result. */
    @Column(nullable = false, length = 50)
    private String result;

    @Column(name = "compliance_date", nullable = false)
    private LocalDate complianceDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
