package com.agriserve.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "farmers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long farmerId;

    @Column(nullable = false)
    private String name;

    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;

    @Column(nullable = false)
    private String contactInfo;

    private BigDecimal landSize;

    private String cropType;

    @Enumerated(EnumType.STRING)
    private FarmerStatus status;

    public enum Gender {
        MALE, FEMALE, OTHERS
    }

    public enum FarmerStatus {
        ACTIVE, INACTIVE, PENDING
    }
}