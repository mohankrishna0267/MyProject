package com.agriserve.dto.feedback;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {

    private Long feedbackId;
    private Long farmerId;
    private Long sessionId;
    private Integer rating;
    private String comments;
    private LocalDateTime createdAt;
}