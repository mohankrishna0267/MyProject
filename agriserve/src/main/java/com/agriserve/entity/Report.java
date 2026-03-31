package com.agriserve.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Generated report containing aggregated metrics across various scopes.
 */
@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    /** Report scope: e.g., "DISTRICT", "STATE", "PROGRAM_123". */
    @Column(nullable = false, length = 200)
    private String scope;

    /** JSON or structured text describing metric values. */
    @Column(columnDefinition = "TEXT")
    private String metrics;

    @CreationTimestamp
    @Column(name = "generated_date", updatable = false)
    private LocalDateTime generatedDate;
}
