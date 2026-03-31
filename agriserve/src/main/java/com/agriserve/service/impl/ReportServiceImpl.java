package com.agriserve.service.impl;

import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.report.ReportRequest;
import com.agriserve.dto.report.ReportResponse;
import com.agriserve.entity.Report;
import com.agriserve.repository.ReportRepository;
import com.agriserve.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    public ReportResponse generateReport(ReportRequest request) {
        Report report = Report.builder()
                .scope(request.getScope())
                .metrics(request.getMetrics())
                .build();

        report = reportRepository.save(report);
        return mapToReportResponse(report);
    }

    @Override
    public PagedResponse<ReportResponse> getAllReports(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Report> reportPage = reportRepository.findAll(pageable);
        return PagedResponse.from(reportPage.map(this::mapToReportResponse));
    }

    private ReportResponse mapToReportResponse(Report report) {
        return ReportResponse.builder()
                .reportId(report.getReportId())
                .scope(report.getScope())
                .metrics(report.getMetrics())
                .generatedDate(report.getGeneratedDate())
                .build();
    }
}
