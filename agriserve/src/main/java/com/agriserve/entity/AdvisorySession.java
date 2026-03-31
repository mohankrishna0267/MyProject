package com.agriserve.entity;

import com.agriserve.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a one-on-one advisory session between an extension officer and a farmer,
 * centred on a specific advisory content piece.
 */
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
    @Column(name = "session_id")
    private Long sessionId;

    /** The extension officer conducting the session. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "officer_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_session_officer"))
    private User officer;

    /** The farmer receiving advisory. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farmer_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_session_farmer"))
    private Farmer farmer;

    /** Content discussed in the session. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "content_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_session_content"))
    private AdvisoryContent content;

    @Column(name = "session_date", nullable = false)
    private LocalDateTime sessionDate;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private Status status = Status.SCHEDULED;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
