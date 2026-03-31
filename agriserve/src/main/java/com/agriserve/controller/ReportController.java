package com.agriserve.controller;

import com.agriserve.dto.ApiResponse;
import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.report.ReportRequest;
import com.agriserve.dto.report.ReportResponse;
import com.agriserve.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PROGRAM_MANAGER', 'GOVERNMENT_AUDITOR', 'ADMIN')")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReportResponse> generateReport(@Valid @RequestBody ReportRequest request) {
        return ApiResponse.success("Report generated", reportService.generateReport(request));
    }

    @GetMapping
    public ApiResponse<PagedResponse<ReportResponse>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(reportService.getAllReports(page, size));
    }
}
