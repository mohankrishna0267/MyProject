package com.agriserve.dto.feedback;

import com.agriserve.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String programTitle;
    private BigDecimal score;
    private LocalDate metricDate;
    private Status status;
    private LocalDateTime createdAt;
}
