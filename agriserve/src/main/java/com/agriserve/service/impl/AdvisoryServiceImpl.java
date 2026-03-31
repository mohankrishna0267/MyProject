package com.agriserve.service.impl;

import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.advisory.AdvisoryContentRequest;
import com.agriserve.dto.advisory.AdvisoryContentResponse;
import com.agriserve.dto.advisory.SessionRequest;
import com.agriserve.dto.advisory.SessionResponse;
import com.agriserve.entity.AdvisoryContent;
import com.agriserve.entity.AdvisorySession;
import com.agriserve.entity.Farmer;
import com.agriserve.entity.User;
import com.agriserve.enums.ContentCategory;
import com.agriserve.enums.Status;
import com.agriserve.exception.ResourceNotFoundException;
import com.agriserve.exception.ValidationException;
import com.agriserve.repository.AdvisoryContentRepository;
import com.agriserve.repository.AdvisorySessionRepository;
import com.agriserve.repository.FarmerRepository;
import com.agriserve.repository.UserRepository;
import com.agriserve.service.AdvisoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdvisoryServiceImpl implements AdvisoryService {

    private final AdvisoryContentRepository contentRepository;
    private final AdvisorySessionRepository sessionRepository;
    private final FarmerRepository farmerRepository;
    private final UserRepository userRepository;

    private final String uploadDir = System.getProperty("user.dir") + "/uploads/advisory/";

    @Override
    public AdvisoryContentResponse createContent(AdvisoryContentRequest request) {
        AdvisoryContent content = AdvisoryContent.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .status(request.getStatus())
                .build();

        content = contentRepository.save(content);
        return mapToContentResponse(content);
    }

    @Override
    public AdvisoryContentResponse getContentById(Long contentId) {
        AdvisoryContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("AdvisoryContent", "id", contentId));
        return mapToContentResponse(content);
    }

    @Override
    public PagedResponse<AdvisoryContentResponse> getContents(ContentCategory category, Status status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdvisoryContent> contentPage;

        if (category != null && status != null) {
            contentPage = contentRepository.findByCategoryAndStatus(category, status, pageable);
        } else if (category != null) {
            contentPage = contentRepository.findByCategory(category, pageable);
        } else if (status != null) {
            contentPage = contentRepository.findByStatus(status, pageable);
        } else {
            contentPage = contentRepository.findAll(pageable);
        }

        return PagedResponse.from(contentPage.map(this::mapToContentResponse));
    }

    @Override
    public AdvisoryContentResponse uploadContentFile(Long contentId, MultipartFile file) {
        AdvisoryContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("AdvisoryContent", "id", contentId));

        if (file.isEmpty()) {
            throw new ValidationException("Cannot upload empty file");
        }

        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, file.getBytes());

            content.setFileUri(filePath.toString());
            content = contentRepository.save(content);

            return mapToContentResponse(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public SessionResponse scheduleSession(SessionRequest request) {
        User officer = userRepository.findById(request.getOfficerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getOfficerId()));

        Farmer farmer = farmerRepository.findById(request.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer", "id", request.getFarmerId()));

        AdvisoryContent content = contentRepository.findById(request.getContentId())
                .orElseThrow(() -> new ResourceNotFoundException("AdvisoryContent", "id", request.getContentId()));

        AdvisorySession session = AdvisorySession.builder()
                .officer(officer)
                .farmer(farmer)
                .content(content)
                .sessionDate(request.getSessionDate())
                .status(request.getStatus())
                .build();

        session = sessionRepository.save(session);
        return mapToSessionResponse(session);
    }

    @Override
    public SessionResponse getSessionById(Long sessionId) {
        AdvisorySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("AdvisorySession", "id", sessionId));
        return mapToSessionResponse(session);
    }

    @Override
    public PagedResponse<SessionResponse> getSessionsByFarmer(Long farmerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdvisorySession> sessionPage = sessionRepository.findByFarmer_FarmerId(farmerId, pageable);
        return PagedResponse.from(sessionPage.map(this::mapToSessionResponse));
    }

    private AdvisoryContentResponse mapToContentResponse(AdvisoryContent content) {
        return AdvisoryContentResponse.builder()
                .contentId(content.getContentId())
                .title(content.getTitle())
                .description(content.getDescription())
                .fileUri(content.getFileUri())
                .category(content.getCategory())
                .status(content.getStatus())
                .uploadedDate(content.getUploadedDate())
                .updatedAt(content.getUpdatedAt())
                .build();
    }

    private SessionResponse mapToSessionResponse(AdvisorySession session) {
        return SessionResponse.builder()
                .sessionId(session.getSessionId())
                .officerId(session.getOfficer().getUserId())
                .officerName(session.getOfficer().getName())
                .farmerId(session.getFarmer().getFarmerId())
                .farmerName(session.getFarmer().getName())
                .contentId(session.getContent().getContentId())
                .contentTitle(session.getContent().getTitle())
                .sessionDate(session.getSessionDate())
                .feedback(session.getFeedback())
                .status(session.getStatus())
                .createdAt(session.getCreatedAt())
                .build();
    }
}
