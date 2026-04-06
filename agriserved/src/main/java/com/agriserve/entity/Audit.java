package com.agriserve.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Long auditId;

    @ManyToOne
    @JoinColumn(name = "officer_id", nullable = false)
    private User officer;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String scope;

    @Column(columnDefinition = "TEXT")
    private String findings;

    @Column(nullable = false)
    private LocalDate auditDate;

    @Enumerated(EnumType.STRING)
    private AuditStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum AuditStatus {
        OPEN, IN_PROGRESS, CLOSED
    }
}