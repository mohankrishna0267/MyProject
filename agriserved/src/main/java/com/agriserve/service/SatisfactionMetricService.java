package com.agriserve.service;

import com.agriserve.dto.feedback.SatisfactionMetricResponse;

import java.util.List;

public interface SatisfactionMetricService {

    SatisfactionMetricResponse calculateMetric(Long programId);

    List<SatisfactionMetricResponse> getMetrics(Long programId);
}