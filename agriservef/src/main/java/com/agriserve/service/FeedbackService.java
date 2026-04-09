package com.agriserve.service;

import com.agriserve.dto.request.FeedbackRequest;
import com.agriserve.dto.response.FeedbackResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contract for Feedback & Satisfaction Monitoring.
 */
public interface FeedbackService {

    FeedbackResponse submitFeedback(FeedbackRequest request);

    FeedbackResponse getFeedbackById(Long feedbackId);

    Page<FeedbackResponse> getFeedbackByFarmer(Long farmerId, Pageable pageable);

    Page<FeedbackResponse> getFeedbackBySession(Long sessionId, Pageable pageable);

    Double getAverageRatingBySession(Long sessionId);

    /** Computes and stores a SatisfactionMetric for the given program */
    Double computeAndStoreSatisfactionMetric(Long programId);
}
