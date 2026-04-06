package com.agriserve.service.impl;

import com.agriserve.dto.feedback.SatisfactionMetricResponse;
import com.agriserve.entity.SatisfactionMetric;
import com.agriserve.entity.TrainingProgram;
import com.agriserve.repository.FeedbackRepository;
import com.agriserve.repository.SatisfactionMetricRepository;
import com.agriserve.repository.TrainingProgramRepository;
import com.agriserve.service.SatisfactionMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SatisfactionMetricServiceImpl implements SatisfactionMetricService {

    private final SatisfactionMetricRepository metricRepository;
    private final TrainingProgramRepository programRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    public SatisfactionMetricResponse calculateMetric(Long programId) {

        TrainingProgram program = programRepository.findById(programId).orElseThrow();

        Double avg = feedbackRepository.findAverageRatingByProgramId(programId).orElse(0.0);

        SatisfactionMetric metric = SatisfactionMetric.builder()
                .program(program)
                .score(avg)
                .metricDate(LocalDate.now())
                .build();

        metric = metricRepository.save(metric);

        return SatisfactionMetricResponse.builder()
                .metricId(metric.getMetricId())
                .score(metric.getScore())
                .build();
    }

    @Override
    public List<SatisfactionMetricResponse> getMetrics(Long programId) {

        return metricRepository.findByProgram_ProgramId(programId)
                .stream()
                .map(m -> SatisfactionMetricResponse.builder()
                        .metricId(m.getMetricId())
                        .score(m.getScore())
                        .build())
                .toList();
    }
}