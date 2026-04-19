package com.agriserve.service.impl;

import com.agriserve.entity.SatisfactionMetric;
import com.agriserve.entity.TrainingProgram;
import com.agriserve.entity.User;
import com.agriserve.entity.Workshop;
import com.agriserve.entity.enums.MetricType;
import com.agriserve.entity.enums.Status;
import com.agriserve.repository.FeedbackRepository;
import com.agriserve.repository.ParticipationRepository;
import com.agriserve.repository.SatisfactionMetricRepository;
import com.agriserve.repository.UserRepository;
import com.agriserve.repository.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Scheduled service for delayed metric calculation.
 *
 * Metric Timing Fix:
 *   Metrics are NOT computed immediately on workshop completion.
 *   Instead, we wait 24 hours after completion so all farmers have time to submit ratings.
 *
 * Data-flow (strictly separated — Issue 6 / Issue 9):
 *
 *   PROGRAM_SATISFACTION:
 *     Training → Workshop (COMPLETED, completedAt < now - 24h)
 *              → Participation.workshopRating → AVG per program
 *
 *   OFFICER_PERFORMANCE:
 *     Advisory → Session (COMPLETED) → Feedback.rating → AVG per officer
 *
 * Both metrics are stored in the SatisfactionMetric table with distinct MetricType values.
 * They are NEVER mixed in the same query.
 *
 * Schedule: runs daily at 02:00 AM.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MetricSchedulerService {

    private final WorkshopRepository workshopRepository;
    private final ParticipationRepository participationRepository;
    private final FeedbackRepository feedbackRepository;
    private final SatisfactionMetricRepository metricRepository;
    private final UserRepository userRepository;

    /** 24-hour delay from workshop completion before metrics are calculated */
    private static final long METRIC_DELAY_HOURS = 24;

    /**
     * Daily job that runs at 02:00 AM.
     *
     * Step 1: Find all workshops completed > 24 hours ago.
     * Step 2: For each unique TrainingProgram, compute PROGRAM_SATISFACTION metric.
     * Step 3: For each unique Extension Officer in those workshops, compute OFFICER_PERFORMANCE metric.
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void calculatePendingMetrics() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(METRIC_DELAY_HOURS);
        List<Workshop> eligibleWorkshops = workshopRepository.findCompletedWorkshopsReadyForMetrics(cutoff);

        if (eligibleWorkshops.isEmpty()) {
            log.debug("MetricScheduler: no workshops eligible for metric calculation at {}", LocalDateTime.now());
            return;
        }

        log.info("MetricScheduler: processing {} completed workshops for metric calculation", eligibleWorkshops.size());

        Set<Long> processedProgramIds = new HashSet<>();
        Set<Long> processedOfficerIds = new HashSet<>();

        for (Workshop workshop : eligibleWorkshops) {
            TrainingProgram program = workshop.getProgram();
            User officer = workshop.getOfficer();

            // ── PROGRAM_SATISFACTION: one metric per unique program ───────
            if (program != null && !processedProgramIds.contains(program.getProgramId())) {
                computeProgramSatisfactionMetric(program);
                processedProgramIds.add(program.getProgramId());
            }

            // ── OFFICER_PERFORMANCE: one metric per unique officer ─────────
            if (officer != null && !processedOfficerIds.contains(officer.getUserId())) {
                computeOfficerPerformanceMetric(officer);
                processedOfficerIds.add(officer.getUserId());
            }
        }

        log.info("MetricScheduler: completed. Programs processed: {}, Officers processed: {}",
                processedProgramIds.size(), processedOfficerIds.size());
    }

    /**
     * Computes PROGRAM_SATISFACTION for a training program.
     * Source: AVG(Participation.workshopRating) for all rated participations in the program.
     * This uses TRAINING data ONLY — no Feedback/Advisory data is mixed in.
     */
    private void computeProgramSatisfactionMetric(TrainingProgram program) {
        Double avg = participationRepository.findAverageWorkshopRatingByProgramId(program.getProgramId());
        double score = avg != null ? Math.round(avg * 100.0) / 100.0 : 0.0;

        SatisfactionMetric metric = SatisfactionMetric.builder()
                .program(program)
                .metricType(MetricType.PROGRAM_SATISFACTION)
                .score(score)
                .status(Status.ACTIVE)
                .build();
        metricRepository.save(metric);
        log.info("PROGRAM_SATISFACTION metric stored for program {}: score={}", program.getProgramId(), score);
    }

    /**
     * Computes OFFICER_PERFORMANCE for an extension officer.
     * Source: AVG(Feedback.rating) on all advisory sessions handled by the officer.
     * This uses ADVISORY data ONLY — no Participation/Training data is mixed in.
     */
    private void computeOfficerPerformanceMetric(User officer) {
        Double avg = feedbackRepository.findAverageRatingByOfficerId(officer.getUserId());
        double score = avg != null ? Math.round(avg * 100.0) / 100.0 : 0.0;

        SatisfactionMetric metric = SatisfactionMetric.builder()
                .officer(officer)
                .metricType(MetricType.OFFICER_PERFORMANCE)
                .score(score)
                .status(Status.ACTIVE)
                .build();
        metricRepository.save(metric);
        log.info("OFFICER_PERFORMANCE metric stored for officer {}: score={}", officer.getUserId(), score);
    }
}
