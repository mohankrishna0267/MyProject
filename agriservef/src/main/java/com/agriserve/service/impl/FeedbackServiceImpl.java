package com.agriserve.service.impl;

import com.agriserve.dto.request.FeedbackRequest;
import com.agriserve.dto.response.FeedbackResponse;
import com.agriserve.entity.AdvisorySession;
import com.agriserve.entity.Farmer;
import com.agriserve.entity.Feedback;
import com.agriserve.entity.SatisfactionMetric;
import com.agriserve.entity.enums.Status;
import com.agriserve.exception.BusinessException;
import com.agriserve.exception.ResourceNotFoundException;
import com.agriserve.repository.AdvisorySessionRepository;
import com.agriserve.repository.FarmerRepository;
import com.agriserve.repository.FeedbackRepository;
import com.agriserve.repository.SatisfactionMetricRepository;
import com.agriserve.repository.TrainingProgramRepository;
import com.agriserve.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles feedback submission and satisfaction metric computation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FarmerRepository farmerRepository;
    private final AdvisorySessionRepository sessionRepository;
    private final SatisfactionMetricRepository metricRepository;
    private final TrainingProgramRepository programRepository;

    @Override
    @Transactional
    public FeedbackResponse submitFeedback(FeedbackRequest request) {
        Farmer farmer = farmerRepository.findById(request.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer", "id", request.getFarmerId()));
        AdvisorySession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("AdvisorySession", "id", request.getSessionId()));

        // Enforce one feedback per farmer per session
        feedbackRepository.findByFarmer_FarmerIdAndSession_SessionId(
                request.getFarmerId(), request.getSessionId())
                .ifPresent(f -> {
                    throw new BusinessException("Feedback already submitted for this session by this farmer");
                });

        Feedback feedback = Feedback.builder()
                .farmer(farmer)
                .session(session)
                .rating(request.getRating())
                .comments(request.getComments())
                .build();
        return FeedbackResponse.from(feedbackRepository.save(feedback));
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackResponse getFeedbackById(Long feedbackId) {
        return FeedbackResponse.from(feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback", "id", feedbackId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponse> getFeedbackByFarmer(Long farmerId, Pageable pageable) {
        return feedbackRepository.findAllByFarmer_FarmerId(farmerId, pageable).map(FeedbackResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponse> getFeedbackBySession(Long sessionId, Pageable pageable) {
        return feedbackRepository.findAllBySession_SessionId(sessionId, pageable).map(FeedbackResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRatingBySession(Long sessionId) {
        Double avg = feedbackRepository.findAverageRatingBySessionId(sessionId);
        return avg != null ? Math.round(avg * 100.0) / 100.0 : 0.0;
    }

    @Override
    @Transactional
    public Double computeAndStoreSatisfactionMetric(Long programId) {
        programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("TrainingProgram", "id", programId));

        Double avg = feedbackRepository.findAverageRatingByProgramId(programId);
        double score = avg != null ? Math.round(avg * 100.0) / 100.0 : 0.0;

        // Persist the metric snapshot
        SatisfactionMetric metric = SatisfactionMetric.builder()
                .program(programRepository.getReferenceById(programId))
                .score(score)
                .status(Status.ACTIVE)
                .build();
        metricRepository.save(metric);
        log.info("Satisfaction metric computed for program {}: {}", programId, score);
        return score;
    }
}
