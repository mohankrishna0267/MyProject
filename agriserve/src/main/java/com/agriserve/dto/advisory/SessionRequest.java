package com.agriserve.dto.advisory;

import com.agriserve.enums.Status;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/** Request DTO for booking an advisory session. */
@Data
public class SessionRequest {

    @NotNull(message = "Officer ID is required")
    private Long officerId;

    @NotNull(message = "Farmer ID is required")
    private Long farmerId;

    @NotNull(message = "Content ID is required")
    private Long contentId;

    @NotNull(message = "Session date/time is required")
    @Future(message = "Session date must be in the future")
    private LocalDateTime sessionDate;

    private Status status = Status.SCHEDULED;
}
