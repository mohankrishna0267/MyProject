package com.agriserve.dto.training;

import com.agriserve.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkshopResponse {

    private Long workshopId;
    private Long programId;
    private String programTitle;
    private Long officerId;
    private String officerName;
    private String location;
    private LocalDateTime workshopDate;
    private Status status;
    private LocalDateTime createdAt;
}
