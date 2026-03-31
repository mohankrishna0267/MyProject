package com.agriserve.controller;

import com.agriserve.dto.ApiResponse;
import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.feedback.FeedbackRequest;
import com.agriserve.dto.feedback.FeedbackResponse;
import com.agriserve.dto.feedback.SatisfactionMetricResponse;
import com.agriserve.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('FARMER', 'ADMIN')")
    public ApiResponse<FeedbackResponse> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        return ApiResponse.success("Feedback submitted", feedbackService.submitFeedback(request));
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("hasAnyRole('EXTENSION_OFFICER', 'PROGRAM_MANAGER', 'ADMIN')")
    public ApiResponse<PagedResponse<FeedbackResponse>> getFeedbackBySession(
            @PathVariable Long sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(feedbackService.getFeedbackBySession(sessionId, page, size));
    }

    @PostMapping("/metrics/program/{programId}")
    @PreAuthorize("hasAnyRole('PROGRAM_MANAGER', 'ADMIN')")
    public ApiResponse<SatisfactionMetricResponse> calculateMetricForProgram(@PathVariable Long programId) {
        return ApiResponse.success("Metric calculated", feedbackService.calculateAndSaveMetricForProgram(programId));
    }

    @GetMapping("/metrics/program/{programId}")
    public ApiResponse<PagedResponse<SatisfactionMetricResponse>> getMetricsForProgram(
            @PathVariable Long programId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(feedbackService.getMetricsForProgram(programId, page, size));
    }
}
