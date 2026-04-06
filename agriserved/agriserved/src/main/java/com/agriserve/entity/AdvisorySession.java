package com.agriserve.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "advisory_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvisorySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    // Farmer who requested the session
    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    // Officer who handles the session
    @ManyToOne
    @JoinColumn(name = "officer_id", nullable = false)
    private User officer;

    // Optional: linked advisory content
    @ManyToOne
    @JoinColumn(name = "content_id")
    private AdvisoryContent content;

    // Session scheduling
    @Column(nullable = false)
    private LocalDateTime sessionDate;

    private LocalDateTime completedAt;

    // Notes given during session
    @Column(columnDefinition = "TEXT")
    private String notes;

    // Status of session
    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum SessionStatus {
        SCHEDULED, COMPLETED, CANCELLED
    }

}