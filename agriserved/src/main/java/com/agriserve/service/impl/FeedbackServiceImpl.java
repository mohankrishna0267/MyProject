package com.agriserve.service.impl;

import com.agriserve.dto.feedback.FeedbackRequest;
import com.agriserve.dto.feedback.FeedbackResponse;
import com.agriserve.entity.AdvisorySession;
import com.agriserve.entity.Farmer;
import com.agriserve.entity.Feedback;
import com.agriserve.repository.AdvisorySessionRepository;
import com.agriserve.repository.FarmerRepository;
import com.agriserve.repository.FeedbackRepository;
import com.agriserve.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FarmerRepository farmerRepository;
    private final AdvisorySessionRepository sessionRepository;

    @Override
    public FeedbackResponse submitFeedback(FeedbackRequest request) {

        if (feedbackRepository.existsByFarmer_FarmerIdAndSession_SessionId(
                request.getFarmerId(), request.getSessionId())) {
            throw new RuntimeException("Feedback already submitted");
        }

        Farmer farmer = farmerRepository.findById(request.getFarmerId()).orElseThrow();
        AdvisorySession session = sessionRepository.findById(request.getSessionId()).orElseThrow();

        Feedback feedback = Feedback.builder()
                .farmer(farmer)
                .session(session)
                .rating(request.getRating())
                .comments(request.getComments())
                .build();

        feedback = feedbackRepository.save(feedback);

        return FeedbackResponse.builder()
                .feedbackId(feedback.getFeedbackId())
                .rating(feedback.getRating())
                .comments(feedback.getComments())
                .build();
    }

    @Override
    public List<FeedbackResponse> getFeedbackBySession(Long sessionId) {

        return feedbackRepository.findBySession_SessionId(sessionId)
                .stream()
                .map(f -> FeedbackResponse.builder()
                        .feedbackId(f.getFeedbackId())
                        .rating(f.getRating())
                        .comments(f.getComments())
                        .build())
                .toList();
    }
}