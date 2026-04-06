package com.agriserve.dto.feedback;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FeedbackRequest {

    @NotNull
    private Long farmerId;

    @NotNull
    private Long sessionId;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;

    private String comments;
}