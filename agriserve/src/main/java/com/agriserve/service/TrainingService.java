package com.agriserve.service;

import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.training.ParticipationRequest;
import com.agriserve.dto.training.ParticipationResponse;
import com.agriserve.dto.training.TrainingProgramRequest;
import com.agriserve.dto.training.TrainingProgramResponse;
import com.agriserve.dto.training.WorkshopRequest;
import com.agriserve.dto.training.WorkshopResponse;

public interface TrainingService {

    TrainingProgramResponse createProgram(TrainingProgramRequest request);

    TrainingProgramResponse getProgramById(Long programId);

    PagedResponse<TrainingProgramResponse> getAllPrograms(int page, int size);

    WorkshopResponse createWorkshop(WorkshopRequest request);

    WorkshopResponse getWorkshopById(Long workshopId);

    PagedResponse<WorkshopResponse> getWorkshopsByProgram(Long programId, int page, int size);

    ParticipationResponse registerFarmerForWorkshop(ParticipationRequest request);

    PagedResponse<ParticipationResponse> getParticipationsByWorkshop(Long workshopId, int page, int size);
}
