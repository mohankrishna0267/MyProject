package com.agriserve.dto.feedback;

import jakarta.validation.constraints.*;
import lombok.Data;

/** Request DTO for submitting feedback for an advisory session. */
@Data
public class FeedbackRequest {

    @NotNull(message = "Farmer ID is required")
    private Long farmerId;

    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 1000, message = "Comments must not exceed 1000 characters")
    private String comments;
}
