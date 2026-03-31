package com.agriserve.entity;

import com.agriserve.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A physical or virtual workshop event belonging to a training program.
 */
@Entity
@Table(name = "workshops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workshop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workshop_id")
    private Long workshopId;

    /** Training program this workshop belongs to. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "program_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_workshop_program"))
    private TrainingProgram program;

    /** Extension officer facilitating the workshop. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "officer_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_workshop_officer"))
    private User officer;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(name = "workshop_date", nullable = false)
    private LocalDateTime workshopDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private Status status = Status.SCHEDULED;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /* ── Relationships ──────────────────────────────────── */

    @OneToMany(mappedBy = "workshop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Participation> participations = new ArrayList<>();
}
