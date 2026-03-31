package com.agriserve.dto.training;

import com.agriserve.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/** Request DTO for creating or updating a training program. */
@Data
public class TrainingProgramRequest {

    @NotBlank(message = "Program title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private Status status = Status.DRAFT;
}
