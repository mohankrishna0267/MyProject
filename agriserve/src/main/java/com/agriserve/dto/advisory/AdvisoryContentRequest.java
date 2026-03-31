package com.agriserve.dto.advisory;

import com.agriserve.enums.ContentCategory;
import com.agriserve.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Request DTO for creating or updating advisory content. */
@Data
public class AdvisoryContentRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    private String description;

    /** Optional file URI; set server-side after upload. */
    private String fileUri;

    @NotNull(message = "Category is required")
    private ContentCategory category;

    private Status status = Status.DRAFT;
}
