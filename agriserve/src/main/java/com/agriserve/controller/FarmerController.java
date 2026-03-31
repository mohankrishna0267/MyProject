package com.agriserve.controller;

import com.agriserve.dto.ApiResponse;
import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.farmer.DocumentResponse;
import com.agriserve.dto.farmer.FarmerRequest;
import com.agriserve.dto.farmer.FarmerResponse;
import com.agriserve.enums.DocumentType;
import com.agriserve.service.FarmerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/farmers")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('FARMER', 'EXTENSION_OFFICER', 'ADMIN')")
    public ApiResponse<FarmerResponse> registerFarmer(@Valid @RequestBody FarmerRequest request) {
        return ApiResponse.success("Farmer profile created", farmerService.registerFarmer(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('FARMER', 'EXTENSION_OFFICER', 'ADMIN', 'PROGRAM_MANAGER')")
    public ApiResponse<FarmerResponse> getFarmer(@PathVariable Long id) {
        return ApiResponse.success(farmerService.getFarmerById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EXTENSION_OFFICER', 'ADMIN', 'PROGRAM_MANAGER')")
    public ApiResponse<PagedResponse<FarmerResponse>> searchFarmers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(farmerService.searchFarmers(keyword, page, size));
    }

    @PostMapping("/{id}/documents")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('FARMER', 'ADMIN')")
    public ApiResponse<DocumentResponse> uploadDocument(
            @PathVariable Long id,
            @RequestParam DocumentType docType,
            @RequestParam("file") MultipartFile file) {
        return ApiResponse.success("Document uploaded sequentially", farmerService.uploadDocument(id, docType, file));
    }
}
