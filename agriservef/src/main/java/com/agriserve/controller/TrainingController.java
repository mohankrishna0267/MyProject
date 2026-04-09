package com.agriserve.controller;

import com.agriserve.dto.request.ParticipationRequest;
import com.agriserve.dto.request.TrainingProgramRequest;
import com.agriserve.dto.request.WorkshopRequest;
import com.agriserve.dto.response.ApiResponse;
import com.agriserve.dto.response.ParticipationResponse;
import com.agriserve.dto.response.TrainingProgramResponse;
import com.agriserve.dto.response.WorkshopResponse;
import com.agriserve.entity.enums.Status;
import com.agriserve.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Training Programs, Workshops, and Participation Tracking.
 */
@Tag(name = "Training", description = "Training programs, workshops, and participation tracking")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    // ─── Training Programs ────────────────────────────────────────────────────

    @Operation(summary = "Create a training program (Program Manager / Admin)")
    @PostMapping("/programs")
    @PreAuthorize("hasAnyRole('PROGRAM_MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<TrainingProgramResponse>> createProgram(
            @Valid @RequestBody TrainingProgramRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(trainingService.createProgram(request), "Training program created"));
    }

    @Operation(summary = "Get all training programs (filterable by status)")
    @GetMapping("/programs")
    public ResponseEntity<ApiResponse<Page<TrainingProgramResponse>>> getAllPrograms(
            @RequestParam(required = false) Status status,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(trainingService.getAllPrograms(status, pageable)));
    }

    @Operation(summary = "Get training program by ID")
    @GetMapping("/programs/{programId}")
    public ResponseEntity<ApiResponse<TrainingProgramResponse>> getProgramById(@PathVariable Long programId) {
        return ResponseEntity.ok(ApiResponse.success(trainingService.getProgramById(programId)));
    }

    @Operation(summary = "Update a training program (Program Manager / Admin)")
    @PutMapping("/programs/{programId}")
    @PreAuthorize("hasAnyRole('PROGRAM_MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<TrainingProgramResponse>> updateProgram(
            @PathVariable Long programId,
            @Valid @RequestBody TrainingProgramRequest request) {
        return ResponseEntity.ok(ApiResponse.success(trainingService.updateProgram(programId, request), "Program updated"));
    }

    @Operation(summary = "Delete a training program (Admin only)")
    @DeleteMapping("/programs/{programId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProgram(@PathVariable Long programId) {
        trainingService.deleteProgram(programId);
        return ResponseEntity.ok(ApiResponse.success(null, "Training program deleted"));
    }

    // ─── Workshops ────────────────────────────────────────────────────────────

    @Operation(summary = "Create a workshop under a program (Officer / Admin)")
    @PostMapping("/workshops")
    @PreAuthorize("hasAnyRole('EXTENSION_OFFICER', 'PROGRAM_MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<WorkshopResponse>> createWorkshop(
            @Valid @RequestBody WorkshopRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(trainingService.createWorkshop(request), "Workshop created"));
    }

    @Operation(summary = "Get workshops for a program")
    @GetMapping("/programs/{programId}/workshops")
    public ResponseEntity<ApiResponse<Page<WorkshopResponse>>> getWorkshopsByProgram(
            @PathVariable Long programId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(trainingService.getWorkshopsByProgram(programId, pageable)));
    }

    @Operation(summary = "Get workshop by ID")
    @GetMapping("/workshops/{workshopId}")
    public ResponseEntity<ApiResponse<WorkshopResponse>> getWorkshopById(@PathVariable Long workshopId) {
        return ResponseEntity.ok(ApiResponse.success(trainingService.getWorkshopById(workshopId)));
    }

    @Operation(summary = "Update workshop status (Officer / Admin)")
    @PatchMapping("/workshops/{workshopId}/status")
    @PreAuthorize("hasAnyRole('EXTENSION_OFFICER', 'PROGRAM_MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<WorkshopResponse>> updateWorkshopStatus(
            @PathVariable Long workshopId,
            @RequestParam Status status) {
        return ResponseEntity.ok(ApiResponse.success(trainingService.updateWorkshopStatus(workshopId, status)));
    }

    // ─── Participation ────────────────────────────────────────────────────────

    @Operation(summary = "Register farmer participation in a workshop")
    @PostMapping("/participation")
    @PreAuthorize("hasAnyRole('EXTENSION_OFFICER', 'PROGRAM_MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<ParticipationResponse>> registerParticipation(
            @Valid @RequestBody ParticipationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(trainingService.registerParticipation(request), "Participation registered"));
    }

    @Operation(summary = "Update participation attendance / feedback")
    @PutMapping("/participation/{participationId}")
    @PreAuthorize("hasAnyRole('EXTENSION_OFFICER', 'PROGRAM_MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<ParticipationResponse>> updateParticipation(
            @PathVariable Long participationId,
            @Valid @RequestBody ParticipationRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                trainingService.updateParticipation(participationId, request), "Participation updated"));
    }

    @Operation(summary = "Get all participants for a workshop")
    @GetMapping("/workshops/{workshopId}/participation")
    public ResponseEntity<ApiResponse<Page<ParticipationResponse>>> getParticipationByWorkshop(
            @PathVariable Long workshopId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(
                trainingService.getParticipationsByWorkshop(workshopId, pageable)));
    }

    @Operation(summary = "Get all workshops a farmer participated in")
    @GetMapping("/participation/farmer/{farmerId}")
    public ResponseEntity<ApiResponse<Page<ParticipationResponse>>> getParticipationByFarmer(
            @PathVariable Long farmerId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(
                trainingService.getParticipationsByFarmer(farmerId, pageable)));
    }
}
