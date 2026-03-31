package com.agriserve.controller;

import com.agriserve.dto.ApiResponse;
import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.training.ParticipationRequest;
import com.agriserve.dto.training.ParticipationResponse;
import com.agriserve.dto.training.TrainingProgramRequest;
import com.agriserve.dto.training.TrainingProgramResponse;
import com.agriserve.dto.training.WorkshopRequest;
import com.agriserve.dto.training.WorkshopResponse;
import com.agriserve.service.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    // --- Programs ---

    @PostMapping("/programs")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('PROGRAM_MANAGER', 'ADMIN')")
    public ApiResponse<TrainingProgramResponse> createProgram(@Valid @RequestBody TrainingProgramRequest request) {
        return ApiResponse.success("Program created", trainingService.createProgram(request));
    }

    @GetMapping("/programs")
    public ApiResponse<PagedResponse<TrainingProgramResponse>> getAllPrograms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(trainingService.getAllPrograms(page, size));
    }

    // --- Workshops ---

    @PostMapping("/workshops")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('PROGRAM_MANAGER', 'EXTENSION_OFFICER', 'ADMIN')")
    public ApiResponse<WorkshopResponse> createWorkshop(@Valid @RequestBody WorkshopRequest request) {
        return ApiResponse.success("Workshop created", trainingService.createWorkshop(request));
    }

    @GetMapping("/programs/{programId}/workshops")
    public ApiResponse<PagedResponse<WorkshopResponse>> getWorkshopsByProgram(
            @PathVariable Long programId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(trainingService.getWorkshopsByProgram(programId, page, size));
    }

    // --- Participations ---

    @PostMapping("/workshops/register")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('FARMER', 'EXTENSION_OFFICER', 'ADMIN')")
    public ApiResponse<ParticipationResponse> registerForWorkshop(@Valid @RequestBody ParticipationRequest request) {
        return ApiResponse.success("Registered for workshop", trainingService.registerFarmerForWorkshop(request));
    }
}
