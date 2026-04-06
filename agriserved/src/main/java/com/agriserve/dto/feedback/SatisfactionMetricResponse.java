package com.agriserve.dto.feedback;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SatisfactionMetricResponse {

    private Long metricId;
    private Long programId;
    private double score;
    private LocalDate metricDate;
    private LocalDateTime createdAt;
}