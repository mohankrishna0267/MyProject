package com.agriserve.service;

import com.agriserve.dto.feedback.FeedbackRequest;
import com.agriserve.dto.feedback.FeedbackResponse;

import java.util.List;

public interface FeedbackService {

    FeedbackResponse submitFeedback(FeedbackRequest request);
    Double getAverageRatingForOfficer(Long officerId);
    List<FeedbackResponse> getFeedbackBySession(Long sessionId);
}