package com.agriserve.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "advisory_content")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvisoryContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String fileUri;

    @Enumerated(EnumType.STRING)
    private ContentCategory category;

    @Enumerated(EnumType.STRING)
    private ContentStatus status;

    private LocalDateTime uploadedDate;

    public enum ContentCategory {
        CROP, SOIL, MARKET
    }

    public enum ContentStatus {
        ACTIVE, INACTIVE
    }
}