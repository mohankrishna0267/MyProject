package com.agriserve.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "participations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participationId;

    @ManyToOne
    @JoinColumn(name = "workshop_id")
    private Workshop workshop;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    private String feedback;

    public enum AttendanceStatus {
        INVITED, ATTENDED, ABSENT
    }
}