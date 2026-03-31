package com.agriserve.service.impl;

import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.feedback.FeedbackRequest;
import com.agriserve.dto.feedback.FeedbackResponse;
import com.agriserve.dto.feedback.SatisfactionMetricResponse;
import com.agriserve.entity.AdvisorySession;
import com.agriserve.entity.Farmer;
import com.agriserve.entity.Feedback;
import com.agriserve.entity.SatisfactionMetric;
import com.agriserve.entity.TrainingProgram;
import com.agriserve.enums.Status;
import com.agriserve.exception.DuplicateResourceException;
import com.agriserve.exception.ResourceNotFoundException;
import com.agriserve.repository.AdvisorySessionRepository;
import com.agriserve.repository.FarmerRepository;
import com.agriserve.repository.FeedbackRepository;
import com.agriserve.repository.SatisfactionMetricRepository;
import com.agriserve.repository.TrainingProgramRepository;
import com.agriserve.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FarmerRepository farmerRepository;
    private final AdvisorySessionRepository sessionRepository;
    private final TrainingProgramRepository programRepository;
    private final SatisfactionMetricRepository metricRepository;

    @Override
    public FeedbackResponse submitFeedback(FeedbackRequest request) {
        if (feedbackRepository.existsByFarmer_FarmerIdAndSession_SessionId(request.getFarmerId(), request.getSessionId())) {
            throw new DuplicateResourceException("Farmer has already submitted feedback for this session");
        }

        Farmer farmer = farmerRepository.findById(request.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer", "id", request.getFarmerId()));

        AdvisorySession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("AdvisorySession", "id", request.getSessionId()));

        Feedback feedback = Feedback.builder()
                .farmer(farmer)
                .session(session)
                .rating(request.getRating())
                .comments(request.getComments())
                .build();

        feedback = feedbackRepository.save(feedback);
        return mapToFeedbackResponse(feedback);
    }

    @Override
    public PagedResponse<FeedbackResponse> getFeedbackBySession(Long sessionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Feedback> pageResult = feedbackRepository.findBySession_SessionId(sessionId, pageable);
        return PagedResponse.from(pageResult.map(this::mapToFeedbackResponse));
    }

    @Override
    public PagedResponse<FeedbackResponse> getFeedbackByFarmer(Long farmerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Feedback> pageResult = feedbackRepository.findByFarmer_FarmerId(farmerId, pageable);
        return PagedResponse.from(pageResult.map(this::mapToFeedbackResponse));
    }

    @Override
    public SatisfactionMetricResponse calculateAndSaveMetricForProgram(Long programId) {
        TrainingProgram program = programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("TrainingProgram", "id", programId));

        Double avgRating = feedbackRepository.findAverageRatingByProgramId(programId)
                .orElse(0.0);

        BigDecimal score = BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.HALF_UP);

        SatisfactionMetric metric = SatisfactionMetric.builder()
                .program(program)
                .score(score)
                .metricDate(LocalDate.now())
                .status(Status.ACTIVE)
                .build();

        metric = metricRepository.save(metric);
        return mapToMetricResponse(metric);
    }

    @Override
    public PagedResponse<SatisfactionMetricResponse> getMetricsForProgram(Long programId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SatisfactionMetric> pageResult = metricRepository.findByProgram_ProgramId(programId, pageable);
        return PagedResponse.from(pageResult.map(this::mapToMetricResponse));
    }

    private FeedbackResponse mapToFeedbackResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .feedbackId(feedback.getFeedbackId())
                .farmerId(feedback.getFarmer().getFarmerId())
                .farmerName(feedback.getFarmer().getName())
                .sessionId(feedback.getSession().getSessionId())
                .rating(feedback.getRating())
                .comments(feedback.getComments())
                .createdAt(feedback.getCreatedAt())
                .build();
    }

    private SatisfactionMetricResponse mapToMetricResponse(SatisfactionMetric metric) {
        return SatisfactionMetricResponse.builder()
                .metricId(metric.getMetricId())
                .programId(metric.getProgram().getProgramId())
                .programTitle(metric.getProgram().getTitle())
                .score(metric.getScore())
                .metricDate(metric.getMetricDate())
                .status(metric.getStatus())
                .createdAt(metric.getCreatedAt())
                .build();
    }
}
