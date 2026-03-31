package com.agriserve.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {

    private Long feedbackId;
    private Long farmerId;
    private String farmerName;
    private Long sessionId;
    private Integer rating;
    private String comments;
    private LocalDateTime createdAt;
}
