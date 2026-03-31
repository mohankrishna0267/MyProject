package com.agriserve.service;

import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.farmer.DocumentResponse;
import com.agriserve.dto.farmer.FarmerRequest;
import com.agriserve.dto.farmer.FarmerResponse;
import com.agriserve.enums.DocumentType;
import org.springframework.web.multipart.MultipartFile;

public interface FarmerService {

    FarmerResponse registerFarmer(FarmerRequest request);

    FarmerResponse getFarmerById(Long id);

    PagedResponse<FarmerResponse> searchFarmers(String keyword, int page, int size);

    DocumentResponse uploadDocument(Long farmerId, DocumentType docType, MultipartFile file);

    PagedResponse<DocumentResponse> getFarmerDocuments(Long farmerId, int page, int size);
}
