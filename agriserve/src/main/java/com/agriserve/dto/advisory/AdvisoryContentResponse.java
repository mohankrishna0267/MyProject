package com.agriserve.dto.advisory;

import com.agriserve.enums.ContentCategory;
import com.agriserve.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** Response DTO for advisory content entries. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvisoryContentResponse {

    private Long contentId;
    private String title;
    private String description;
    private String fileUri;
    private ContentCategory category;
    private Status status;
    private LocalDateTime uploadedDate;
    private LocalDateTime updatedAt;
}
