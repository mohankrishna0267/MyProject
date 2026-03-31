package com.agriserve.service;

import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.feedback.FeedbackRequest;
import com.agriserve.dto.feedback.FeedbackResponse;
import com.agriserve.dto.feedback.SatisfactionMetricResponse;

public interface FeedbackService {

    FeedbackResponse submitFeedback(FeedbackRequest request);

    PagedResponse<FeedbackResponse> getFeedbackBySession(Long sessionId, int page, int size);

    PagedResponse<FeedbackResponse> getFeedbackByFarmer(Long farmerId, int page, int size);

    SatisfactionMetricResponse calculateAndSaveMetricForProgram(Long programId);

    PagedResponse<SatisfactionMetricResponse> getMetricsForProgram(Long programId, int page, int size);
}
