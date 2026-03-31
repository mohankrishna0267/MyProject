package com.agriserve.dto.training;

import com.agriserve.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Request DTO for registering a farmer to a workshop. */
@Data
public class ParticipationRequest {

    @NotNull(message = "Workshop ID is required")
    private Long workshopId;

    @NotNull(message = "Farmer ID is required")
    private Long farmerId;

    private AttendanceStatus attendanceStatus = AttendanceStatus.REGISTERED;

    private String feedback;
}
