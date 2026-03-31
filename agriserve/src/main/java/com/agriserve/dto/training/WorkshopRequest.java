package com.agriserve.dto.training;

import com.agriserve.enums.Status;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/** Request DTO for creating or updating a workshop under a training program. */
@Data
public class WorkshopRequest {

    @NotNull(message = "Program ID is required")
    private Long programId;

    @NotNull(message = "Officer ID is required")
    private Long officerId;

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location must not exceed 200 characters")
    private String location;

    @NotNull(message = "Workshop date/time is required")
    @Future(message = "Workshop date must be in the future")
    private LocalDateTime workshopDate;

    private Status status = Status.SCHEDULED;
}
