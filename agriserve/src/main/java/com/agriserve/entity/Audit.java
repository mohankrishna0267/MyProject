package com.agriserve.entity;

import com.agriserve.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Formal audit conducted by a compliance officer or government auditor.
 */
@Entity
@Table(name = "audits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    /** The officer (Compliance Officer / Government Auditor) conducting the audit. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "officer_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_audit_officer"))
    private User officer;

    /** Textual description of the audit scope. */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String scope;

    /** Key findings of the audit. */
    @Column(columnDefinition = "TEXT")
    private String findings;

    @Column(name = "audit_date", nullable = false)
    private LocalDate auditDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private Status status = Status.DRAFT;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
