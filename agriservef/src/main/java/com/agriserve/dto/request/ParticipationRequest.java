package com.agriserve.dto.request;

import com.agriserve.entity.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO for registering or updating farmer participation in a workshop.
 */
@Data
public class ParticipationRequest {

    @NotNull(message = "Workshop ID is required")
    private Long workshopId;

    @NotNull(message = "Farmer ID is required")
    private Long farmerId;

    @NotNull(message = "Attendance status is required")
    private AttendanceStatus attendanceStatus;

    private String feedback;
}
