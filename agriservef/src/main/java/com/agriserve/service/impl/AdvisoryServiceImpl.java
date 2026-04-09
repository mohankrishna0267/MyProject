package com.agriserve.service.impl;

import com.agriserve.dto.request.AdvisoryContentRequest;
import com.agriserve.dto.request.AdvisorySessionRequest;
import com.agriserve.dto.response.AdvisoryContentResponse;
import com.agriserve.dto.response.AdvisorySessionResponse;
import com.agriserve.entity.AdvisoryContent;
import com.agriserve.entity.AdvisorySession;
import com.agriserve.entity.Farmer;
import com.agriserve.entity.User;
import com.agriserve.entity.enums.AdvisoryCategory;
import com.agriserve.entity.enums.Status;
import com.agriserve.exception.ResourceNotFoundException;
import com.agriserve.repository.AdvisoryContentRepository;
import com.agriserve.repository.AdvisorySessionRepository;
import com.agriserve.repository.FarmerRepository;
import com.agriserve.repository.UserRepository;
import com.agriserve.service.AdvisoryService;
import com.agriserve.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manages advisory content and session booking.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdvisoryServiceImpl implements AdvisoryService {

    private final AdvisoryContentRepository contentRepository;
    private final AdvisorySessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final FarmerRepository farmerRepository;
    private final AuditLogService auditLogService;

    // ─── Advisory Content ────────────────────────────────────────────────────

    @Override
    @Transactional
    public AdvisoryContentResponse createContent(AdvisoryContentRequest request, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", authorId));

        AdvisoryContent content = AdvisoryContent.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .fileUri(request.getFileUri())
                .category(request.getCategory())
                .status(request.getStatus() != null ? request.getStatus() : Status.DRAFT)
                .author(author)
                .build();
        AdvisoryContent saved = contentRepository.save(content);
        auditLogService.log(author, "CREATE_ADVISORY_CONTENT", "AdvisoryContent#" + saved.getContentId(), null);
        return AdvisoryContentResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AdvisoryContentResponse getContentById(Long contentId) {
        return AdvisoryContentResponse.from(findContentOrThrow(contentId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdvisoryContentResponse> searchContent(String title, AdvisoryCategory category,
                                                       Status status, Pageable pageable) {
        return contentRepository.searchContent(title, category, status, pageable)
                .map(AdvisoryContentResponse::from);
    }

    @Override
    @Transactional
    public AdvisoryContentResponse updateContent(Long contentId, AdvisoryContentRequest request) {
        AdvisoryContent content = findContentOrThrow(contentId);
        content.setTitle(request.getTitle());
        content.setDescription(request.getDescription());
        content.setFileUri(request.getFileUri());
        content.setCategory(request.getCategory());
        if (request.getStatus() != null) content.setStatus(request.getStatus());
        return AdvisoryContentResponse.from(contentRepository.save(content));
    }

    @Override
    @Transactional
    public void deleteContent(Long contentId) {
        AdvisoryContent content = findContentOrThrow(contentId);
        contentRepository.delete(content);
    }

    // ─── Advisory Sessions ───────────────────────────────────────────────────

    @Override
    @Transactional
    public AdvisorySessionResponse bookSession(AdvisorySessionRequest request) {
        User officer = userRepository.findById(request.getOfficerId())
                .orElseThrow(() -> new ResourceNotFoundException("User (Officer)", "id", request.getOfficerId()));
        Farmer farmer = farmerRepository.findById(request.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer", "id", request.getFarmerId()));

        AdvisoryContent content = null;
        if (request.getContentId() != null) {
            content = findContentOrThrow(request.getContentId());
        }

        AdvisorySession session = AdvisorySession.builder()
                .officer(officer)
                .farmer(farmer)
                .content(content)
                .sessionDate(request.getSessionDate())
                .status(Status.PENDING)
                .build();
        AdvisorySession saved = sessionRepository.save(session);
        auditLogService.log(officer, "BOOK_ADVISORY_SESSION", "AdvisorySession#" + saved.getSessionId(), null);
        return AdvisorySessionResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AdvisorySessionResponse getSessionById(Long sessionId) {
        return AdvisorySessionResponse.from(findSessionOrThrow(sessionId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdvisorySessionResponse> getSessionsByFarmer(Long farmerId, Pageable pageable) {
        return sessionRepository.findAllByFarmer_FarmerId(farmerId, pageable)
                .map(AdvisorySessionResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdvisorySessionResponse> getSessionsByOfficer(Long officerId, Pageable pageable) {
        return sessionRepository.findAllByOfficer_UserId(officerId, pageable)
                .map(AdvisorySessionResponse::from);
    }

    @Override
    @Transactional
    public AdvisorySessionResponse updateSessionStatus(Long sessionId, Status status, String feedback) {
        AdvisorySession session = findSessionOrThrow(sessionId);
        session.setStatus(status);
        if (feedback != null) session.setFeedback(feedback);
        return AdvisorySessionResponse.from(sessionRepository.save(session));
    }

    // ─── Private Helpers ─────────────────────────────────────────────────────

    private AdvisoryContent findContentOrThrow(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdvisoryContent", "id", id));
    }

    private AdvisorySession findSessionOrThrow(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdvisorySession", "id", id));
    }
}
