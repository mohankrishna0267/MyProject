package com.agriserve.entity;

import com.agriserve.enums.DocumentType;
import com.agriserve.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a document uploaded by a farmer for KYC / verification.
 */
@Entity
@Table(name = "farmer_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmerDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farmer_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_doc_farmer"))
    private Farmer farmer;

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type", nullable = false, length = 50)
    private DocumentType docType;

    /** Relative path or URI to the uploaded file on the server / cloud storage. */
    @Column(name = "file_uri", nullable = false)
    private String fileUri;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 30)
    @Builder.Default
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @CreationTimestamp
    @Column(name = "uploaded_date", updatable = false)
    private LocalDateTime uploadedDate;

    /** Notes added by the verifying officer. */
    @Column(name = "review_notes", columnDefinition = "TEXT")
    private String reviewNotes;
}
