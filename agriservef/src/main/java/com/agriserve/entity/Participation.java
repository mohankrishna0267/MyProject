package com.agriserve.entity;

import com.agriserve.entity.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Tracks a farmer's participation and attendance in a specific workshop.
 * Unique per farmer per workshop.
 */
@Entity
@Table(
    name = "participations",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_participation_workshop_farmer",
        columnNames = {"workshop_id", "farmer_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_id")
    private Long participationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false, length = 20)
    @Builder.Default
    private AttendanceStatus attendanceStatus = AttendanceStatus.ABSENT;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
