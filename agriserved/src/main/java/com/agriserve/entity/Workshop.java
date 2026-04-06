package com.agriserve.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private Long workshopId;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private TrainingProgram program;

    @ManyToOne
    @JoinColumn(name = "officer_id")
    private User officer;

    private String location;

    private LocalDateTime workshopDate;

    @Enumerated(EnumType.STRING)
    private WorkshopStatus status;

    public enum WorkshopStatus {
        SCHEDULED, ONGOING, COMPLETED, CANCELLED
    }
}