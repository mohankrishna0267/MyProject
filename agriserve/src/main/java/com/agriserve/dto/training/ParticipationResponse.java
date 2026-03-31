package com.agriserve.dto.training;

import com.agriserve.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationResponse {

    private Long participationId;
    private Long workshopId;
    private String workshopLocation;
    private Long farmerId;
    private String farmerName;
    private AttendanceStatus attendanceStatus;
    private String feedback;
    private LocalDateTime createdAt;
}
