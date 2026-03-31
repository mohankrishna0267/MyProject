package com.agriserve.dto.farmer;

import com.agriserve.enums.DocumentType;
import com.agriserve.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** Response DTO for a farmer document record. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {

    private Long documentId;
    private Long farmerId;
    private DocumentType docType;
    private String fileUri;
    private VerificationStatus verificationStatus;
    private String reviewNotes;
    private LocalDateTime uploadedDate;
}
