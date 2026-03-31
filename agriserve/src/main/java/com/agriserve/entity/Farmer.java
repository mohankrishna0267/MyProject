package com.agriserve.entity;

import com.agriserve.enums.Gender;
import com.agriserve.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a registered farmer in the system.
 * A Farmer may also have a corresponding User account for portal login.
 */
@Entity
@Table(
    name = "farmers",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_farmer_phone", columnNames = "contact_info")
    }
)
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

    @Column(name = "dob")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private Gender gender;

    @Column(columnDefinition = "TEXT")
    private String address;

    /** Primary phone number — unique per farmer. */
    @Column(name = "contact_info", nullable = false, length = 20)
    private String contactInfo;

    /** Total landholding in acres. */
    @Column(name = "land_size", precision = 10, scale = 2)
    private BigDecimal landSize;

    /** Comma-separated crop types; consider a separate table for production systems. */
    @Column(name = "crop_type", length = 255)
    private String cropType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private UserStatus status = UserStatus.PENDING_VERIFICATION;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* ── Relationships ──────────────────────────────────── */

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<FarmerDocument> documents = new ArrayList<>();

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AdvisorySession> advisorySessions = new ArrayList<>();

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Participation> participations = new ArrayList<>();

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Feedback> feedbacks = new ArrayList<>();
}
