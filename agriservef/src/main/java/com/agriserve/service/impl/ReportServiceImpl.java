package com.agriserve.service.impl;

import com.agriserve.dto.request.ReportRequest;
import com.agriserve.dto.response.ReportResponse;
import com.agriserve.entity.Report;
import com.agriserve.entity.User;
import com.agriserve.entity.enums.ReportScope;
import com.agriserve.exception.ResourceNotFoundException;
import com.agriserve.repository.FarmerRepository;
import com.agriserve.repository.ReportRepository;
import com.agriserve.repository.TrainingProgramRepository;
import com.agriserve.repository.UserRepository;
import com.agriserve.repository.AdvisorySessionRepository;
import com.agriserve.repository.FeedbackRepository;
import com.agriserve.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generates and retrieves system reports.
 * Metrics are assembled into a JSON-like string for storage.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final FarmerRepository farmerRepository;
    private final TrainingProgramRepository programRepository;
    private final AdvisorySessionRepository sessionRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    @Transactional
    public ReportResponse generateReport(ReportRequest request, Long generatedByUserId) {
        User generatedBy = userRepository.findById(generatedByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", generatedByUserId));

        // Build a basic metrics snapshot (in production use JSON serialisation)
        String metrics = buildMetricsSnapshot(request);

        Report report = Report.builder()
                .scope(request.getScope())
                .title(request.getTitle() != null ? request.getTitle()
                        : request.getScope().name() + " Report")
                .metrics(metrics)
                .generatedBy(generatedBy)
                .build();
        Report saved = reportRepository.save(report);
        log.info("Report generated: id={} scope={}", saved.getReportId(), saved.getScope());
        return ReportResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResponse getReportById(Long reportId) {
        return ReportResponse.from(reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", reportId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportResponse> getAllReports(ReportScope scope, Pageable pageable) {
        if (scope != null) {
            return reportRepository.findAllByScope(scope, pageable).map(ReportResponse::from);
        }
        return reportRepository.findAll(pageable).map(ReportResponse::from);
    }

    // ─── Private ─────────────────────────────────────────────────────────────

    private String buildMetricsSnapshot(ReportRequest request) {
        long totalFarmers = farmerRepository.count();
        long totalPrograms = programRepository.count();
        long totalSessions = sessionRepository.count();

        return String.format(
            "{\"totalFarmers\": %d, \"totalTrainingPrograms\": %d, \"totalAdvisorySessions\": %d, \"scope\": \"%s\"}",
            totalFarmers, totalPrograms, totalSessions, request.getScope()
        );
    }
}
