package com.agriserve.controller;


import com.agriserve.dto.feedback.SatisfactionMetricResponse;
import com.agriserve.service.SatisfactionMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class SatisfactionMetricController {

    private final SatisfactionMetricService metricService;

    @PostMapping("/program/{programId}")
    public ResponseEntity<SatisfactionMetricResponse> calculateMetric(@PathVariable Long programId) {
        return ResponseEntity.ok(metricService.calculateMetric(programId));
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<SatisfactionMetricResponse>> getMetrics(@PathVariable Long programId) {
        return ResponseEntity.ok(metricService.getMetrics(programId));
    }
}