package com.agriserve.controller;

import com.agriserve.dto.ApiResponse;
import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.advisory.AdvisoryContentRequest;
import com.agriserve.dto.advisory.AdvisoryContentResponse;
import com.agriserve.dto.advisory.SessionRequest;
import com.agriserve.dto.advisory.SessionResponse;
import com.agriserve.enums.ContentCategory;
import com.agriserve.enums.Status;
import com.agriserve.service.AdvisoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/advisory")
@RequiredArgsConstructor
public class AdvisoryController {

    private final AdvisoryService advisoryService;

    // --- Content Endpoints ---

    @PostMapping("/content")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('EXTENSION_OFFICER', 'ADMIN')")
    public ApiResponse<AdvisoryContentResponse> createContent(@Valid @RequestBody AdvisoryContentRequest request) {
        return ApiResponse.success("Content created", advisoryService.createContent(request));
    }

    @PostMapping("/content/{contentId}/upload")
    @PreAuthorize("hasAnyRole('EXTENSION_OFFICER', 'ADMIN')")
    public ApiResponse<AdvisoryContentResponse> uploadContentFile(
            @PathVariable Long contentId,
            @RequestParam("file") MultipartFile file) {
        return ApiResponse.success("File uploaded", advisoryService.uploadContentFile(contentId, file));
    }

    @GetMapping("/content")
    public ApiResponse<PagedResponse<AdvisoryContentResponse>> getContents(
            @RequestParam(required = false) ContentCategory category,
            @RequestParam(required = false) Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(advisoryService.getContents(category, status, page, size));
    }

    // --- Session Endpoints ---

    @PostMapping("/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('EXTENSION_OFFICER', 'ADMIN')")
    public ApiResponse<SessionResponse> scheduleSession(@Valid @RequestBody SessionRequest request) {
        return ApiResponse.success("Session scheduled", advisoryService.scheduleSession(request));
    }

    @GetMapping("/sessions/farmer/{farmerId}")
    @PreAuthorize("hasAnyRole('FARMER', 'EXTENSION_OFFICER', 'ADMIN')")
    public ApiResponse<PagedResponse<SessionResponse>> getSessionsByFarmer(
            @PathVariable Long farmerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(advisoryService.getSessionsByFarmer(farmerId, page, size));
    }
}
