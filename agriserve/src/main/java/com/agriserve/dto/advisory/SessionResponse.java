package com.agriserve.dto.advisory;

import com.agriserve.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** Response DTO for advisory sessions. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {

    private Long sessionId;
    private Long officerId;
    private String officerName;
    private Long farmerId;
    private String farmerName;
    private Long contentId;
    private String contentTitle;
    private LocalDateTime sessionDate;
    private String feedback;
    private Status status;
    private LocalDateTime createdAt;
}
