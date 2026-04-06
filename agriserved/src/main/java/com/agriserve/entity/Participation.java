package com.agriserve.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Min(1)
    @Max(5)
    private Integer rating;

    private String feedback;

    public enum AttendanceStatus {
        INVITED, ATTENDED, ABSENT
    }
}