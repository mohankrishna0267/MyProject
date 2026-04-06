package com.agriserve.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Long complianceId;

    @Column(nullable = false)
    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplianceType entityType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplianceResult result;

    @Column(nullable = false)
    private LocalDate complianceDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum ComplianceType {
        ADVISORY, TRAINING
    }

    public enum ComplianceResult {
        PASS, FAIL, PENDING
    }
}