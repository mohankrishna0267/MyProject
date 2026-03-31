package com.agriserve.repository;

import com.agriserve.entity.FarmerDocument;
import com.agriserve.enums.DocumentType;
import com.agriserve.enums.VerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmerDocumentRepository extends JpaRepository<FarmerDocument, Long> {

    List<FarmerDocument> findByFarmer_FarmerId(Long farmerId);

    Page<FarmerDocument> findByFarmer_FarmerIdAndDocType(Long farmerId, DocumentType docType, Pageable pageable);

    Page<FarmerDocument> findByVerificationStatus(VerificationStatus verificationStatus, Pageable pageable);
}
