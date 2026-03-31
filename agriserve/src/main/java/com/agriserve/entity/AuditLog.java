package com.agriserve.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Audit log entry created automatically via AOP whenever a state-changing
 * operation is performed on a resource.
 */
@Entity
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_log_id")
    private Long auditLogId;

    /** The user who performed the action (nullable for system-generated events). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_auditlog_user"))
    private User user;

    /** Short action description, e.g., "CREATE_FARMER", "UPDATE_ADVISORY". */
    @Column(nullable = false, length = 100)
    private String action;

    /** The resource/entity type that was acted upon. */
    @Column(nullable = false, length = 100)
    private String resource;

    /** Optional ID of the specific resource instance. */
    @Column(name = "resource_id")
    private Long resourceId;

    /** Additional context/details (JSON string or free text). */
    @Column(columnDefinition = "TEXT")
    private String details;

    @CreationTimestamp
    @Column(name = "timestamp", updatable = false)
    private LocalDateTime timestamp;
}
