package com.agriserve.service;

import com.agriserve.dto.request.ReportRequest;
import com.agriserve.dto.response.ReportResponse;
import com.agriserve.entity.enums.ReportScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contract for Reporting & Analytics.
 */
public interface ReportService {

    ReportResponse generateReport(ReportRequest request, Long generatedByUserId);

    ReportResponse getReportById(Long reportId);

    Page<ReportResponse> getAllReports(ReportScope scope, Pageable pageable);
}
