package com.agriserve.controller;

import com.agriserve.dto.compliance.ComplianceRecordRequest;
import com.agriserve.dto.compliance.ComplianceRecordResponse;
import com.agriserve.service.ComplianceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/compliance-records")
@RequiredArgsConstructor
public class ComplianceRecordController {

    private final ComplianceRecordService complianceService;

    @PostMapping
    public ResponseEntity<ComplianceRecordResponse> createRecord(@Valid @RequestBody ComplianceRecordRequest request) {
        return new ResponseEntity<>(complianceService.createRecord(request), HttpStatus.CREATED);
    }

    @GetMapping("/{entityType}/{entityId}")
    public ResponseEntity<List<ComplianceRecordResponse>> getRecords(
            @PathVariable String entityType,
            @PathVariable Long entityId) {

        return ResponseEntity.ok(complianceService.getRecords(entityId, entityType));
    }
}
