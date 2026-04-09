package com.agriserve.service.impl;

import com.agriserve.dto.request.FarmerRequest;
import com.agriserve.dto.response.FarmerDocumentResponse;
import com.agriserve.dto.response.FarmerResponse;
import com.agriserve.entity.Farmer;
import com.agriserve.entity.FarmerDocument;
import com.agriserve.entity.enums.DocumentType;
import com.agriserve.entity.enums.Status;
import com.agriserve.exception.ResourceNotFoundException;
import com.agriserve.repository.FarmerDocumentRepository;
import com.agriserve.repository.FarmerRepository;
import com.agriserve.service.AuditLogService;
import com.agriserve.service.FarmerService;
import com.agriserve.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Manages Farmer profiles and document uploads.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FarmerServiceImpl implements FarmerService {

    private final FarmerRepository farmerRepository;
    private final FarmerDocumentRepository documentRepository;
    private final FileStorageUtil fileStorageUtil;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public FarmerResponse registerFarmer(FarmerRequest request) {
        Farmer farmer = Farmer.builder()
                .name(request.getName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .address(request.getAddress())
                .contactInfo(request.getContactInfo())
                .landSize(request.getLandSize())
                .cropType(request.getCropType())
                .status(Status.PENDING)
                .build();
        Farmer saved = farmerRepository.save(farmer);
        log.info("Farmer registered with ID: {}", saved.getFarmerId());
        return FarmerResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FarmerResponse getFarmerById(Long farmerId) {
        return FarmerResponse.from(findFarmerOrThrow(farmerId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FarmerResponse> getAllFarmers(Pageable pageable) {
        return farmerRepository.findAll(pageable).map(FarmerResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FarmerResponse> searchFarmers(String name, String cropType, Status status, Pageable pageable) {
        return farmerRepository.searchFarmers(name, cropType, status, pageable).map(FarmerResponse::from);
    }

    @Override
    @Transactional
    public FarmerResponse updateFarmer(Long farmerId, FarmerRequest request) {
        Farmer farmer = findFarmerOrThrow(farmerId);
        farmer.setName(request.getName());
        farmer.setDateOfBirth(request.getDateOfBirth());
        farmer.setGender(request.getGender());
        farmer.setAddress(request.getAddress());
        farmer.setContactInfo(request.getContactInfo());
        farmer.setLandSize(request.getLandSize());
        farmer.setCropType(request.getCropType());
        return FarmerResponse.from(farmerRepository.save(farmer));
    }

    @Override
    @Transactional
    public void deleteFarmer(Long farmerId) {
        Farmer farmer = findFarmerOrThrow(farmerId);
        farmerRepository.delete(farmer);
        log.info("Farmer deleted: {}", farmerId);
    }

    @Override
    @Transactional
    public FarmerResponse updateFarmerStatus(Long farmerId, Status status) {
        Farmer farmer = findFarmerOrThrow(farmerId);
        farmer.setStatus(status);
        return FarmerResponse.from(farmerRepository.save(farmer));
    }

    @Override
    @Transactional
    public FarmerDocumentResponse uploadDocument(Long farmerId, DocumentType docType, MultipartFile file) {
        Farmer farmer = findFarmerOrThrow(farmerId);
        String fileUri = fileStorageUtil.storeFile(file, "farmer_" + farmerId);

        FarmerDocument document = FarmerDocument.builder()
                .farmer(farmer)
                .docType(docType)
                .fileUri(fileUri)
                .verificationStatus(Status.PENDING)
                .build();
        return FarmerDocumentResponse.from(documentRepository.save(document));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FarmerDocumentResponse> getFarmerDocuments(Long farmerId, Pageable pageable) {
        findFarmerOrThrow(farmerId);
        return documentRepository.findAllByFarmer_FarmerId(farmerId, pageable)
                .map(FarmerDocumentResponse::from);
    }

    @Override
    @Transactional
    public FarmerDocumentResponse verifyDocument(Long documentId, Status verificationStatus) {
        FarmerDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("FarmerDocument", "id", documentId));
        doc.setVerificationStatus(verificationStatus);
        return FarmerDocumentResponse.from(documentRepository.save(doc));
    }

    private Farmer findFarmerOrThrow(Long farmerId) {
        return farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer", "id", farmerId));
    }
}
