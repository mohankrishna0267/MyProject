package com.agriserve.service;

import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.report.ReportRequest;
import com.agriserve.dto.report.ReportResponse;

public interface ReportService {
    ReportResponse generateReport(ReportRequest request);
    PagedResponse<ReportResponse> getAllReports(int page, int size);
}
