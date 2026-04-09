package com.agriserve.entity;

import com.agriserve.entity.enums.Gender;
import com.agriserve.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a registered farmer in the AgriServe system.
 * A farmer may optionally have a corresponding User account for portal access.
 */
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
    @Column(name = "farmer_id")
    private Long farmerId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    @Column(columnDefinition = "TEXT")
    private String address;

    /** JSON or CSV of contact info (phone, email, whatsapp) */
    @Column(name = "contact_info", length = 300)
    private String contactInfo;

    /** Land size in acres */
    @Column(name = "land_size")
    private Double landSize;

    /** Primary crop grown (e.g. Wheat, Rice, Cotton) */
    @Column(name = "crop_type", length = 100)
    private String cropType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Status status = Status.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ─── Relationships ───────────────────────────────────────────────────────

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FarmerDocument> documents = new ArrayList<>();

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL)
    @Builder.Default
    private List<AdvisorySession> advisorySessions = new ArrayList<>();

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Participation> participations = new ArrayList<>();

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Feedback> feedbacks = new ArrayList<>();
}
