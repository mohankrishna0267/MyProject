package com.agriserve.controller;

import com.agriserve.dto.request.FeedbackRequest;
import com.agriserve.dto.response.ApiResponse;
import com.agriserve.dto.response.FeedbackResponse;
import com.agriserve.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Feedback & Satisfaction Monitoring.
 */
@Tag(name = "Feedback", description = "Farmer feedback submission and satisfaction metrics")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "Submit feedback for an advisory session (Farmer / Officer)")
    @PostMapping
    @PreAuthorize("hasAnyRole('FARMER', 'EXTENSION_OFFICER', 'ADMIN')")
    public ResponseEntity<ApiResponse<FeedbackResponse>> submitFeedback(
            @Valid @RequestBody FeedbackRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(feedbackService.submitFeedback(request), "Feedback submitted"));
    }

    @Operation(summary = "Get feedback by ID")
    @GetMapping("/{feedbackId}")
    public ResponseEntity<ApiResponse<FeedbackResponse>> getFeedbackById(@PathVariable Long feedbackId) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getFeedbackById(feedbackId)));
    }

    @Operation(summary = "Get all feedback submitted by a farmer")
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<ApiResponse<Page<FeedbackResponse>>> getFeedbackByFarmer(
            @PathVariable Long farmerId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getFeedbackByFarmer(farmerId, pageable)));
    }

    @Operation(summary = "Get all feedback for an advisory session")
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<Page<FeedbackResponse>>> getFeedbackBySession(
            @PathVariable Long sessionId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getFeedbackBySession(sessionId, pageable)));
    }

    @Operation(summary = "Get average rating for an advisory session")
    @GetMapping("/session/{sessionId}/average-rating")
    public ResponseEntity<ApiResponse<Double>> getAverageRating(@PathVariable Long sessionId) {
        return ResponseEntity.ok(ApiResponse.success(feedbackService.getAverageRatingBySession(sessionId)));
    }

    @Operation(summary = "Compute and persist satisfaction metric for a training program (Manager / Admin)")
    @PostMapping("/satisfaction/program/{programId}/compute")
    @PreAuthorize("hasAnyRole('PROGRAM_MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Double>> computeSatisfactionMetric(@PathVariable Long programId) {
        Double score = feedbackService.computeAndStoreSatisfactionMetric(programId);
        return ResponseEntity.ok(ApiResponse.success(score, "Satisfaction metric computed and stored"));
    }
}
