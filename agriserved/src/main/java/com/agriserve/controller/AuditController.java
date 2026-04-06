package com.agriserve.controller;

import com.agriserve.dto.compliance.AuditRequest;
import com.agriserve.dto.compliance.AuditResponse;
import com.agriserve.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/audits")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @PostMapping
    public ResponseEntity<AuditResponse> createAudit(@Valid @RequestBody AuditRequest request) {
        return new ResponseEntity<>(auditService.createAudit(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AuditResponse>> getAllAudits() {
        return ResponseEntity.ok(auditService.getAllAudits());
    }
}