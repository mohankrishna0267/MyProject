package com.agriserve.service;

import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.advisory.AdvisoryContentRequest;
import com.agriserve.dto.advisory.AdvisoryContentResponse;
import com.agriserve.dto.advisory.SessionRequest;
import com.agriserve.dto.advisory.SessionResponse;
import com.agriserve.enums.ContentCategory;
import com.agriserve.enums.Status;
import org.springframework.web.multipart.MultipartFile;

public interface AdvisoryService {

    AdvisoryContentResponse createContent(AdvisoryContentRequest request);

    AdvisoryContentResponse getContentById(Long contentId);

    PagedResponse<AdvisoryContentResponse> getContents(ContentCategory category, Status status, int page, int size);

    AdvisoryContentResponse uploadContentFile(Long contentId, MultipartFile file);

    SessionResponse scheduleSession(SessionRequest request);

    SessionResponse getSessionById(Long sessionId);

    PagedResponse<SessionResponse> getSessionsByFarmer(Long farmerId, int page, int size);
}
