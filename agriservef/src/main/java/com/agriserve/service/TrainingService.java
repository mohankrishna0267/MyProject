package com.agriserve.service;

import com.agriserve.dto.request.ParticipationRequest;
import com.agriserve.dto.request.TrainingProgramRequest;
import com.agriserve.dto.request.WorkshopRequest;
import com.agriserve.dto.response.ParticipationResponse;
import com.agriserve.dto.response.TrainingProgramResponse;
import com.agriserve.dto.response.WorkshopResponse;
import com.agriserve.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contract for Training Program & Workshop management.
 */
public interface TrainingService {

    // Programs
    TrainingProgramResponse createProgram(TrainingProgramRequest request);

    TrainingProgramResponse getProgramById(Long programId);

    Page<TrainingProgramResponse> getAllPrograms(Status status, Pageable pageable);

    TrainingProgramResponse updateProgram(Long programId, TrainingProgramRequest request);

    void deleteProgram(Long programId);

    // Workshops
    WorkshopResponse createWorkshop(WorkshopRequest request);

    WorkshopResponse getWorkshopById(Long workshopId);

    Page<WorkshopResponse> getWorkshopsByProgram(Long programId, Pageable pageable);

    WorkshopResponse updateWorkshopStatus(Long workshopId, Status status);

    // Participation
    ParticipationResponse registerParticipation(ParticipationRequest request);

    ParticipationResponse updateParticipation(Long participationId, ParticipationRequest request);

    Page<ParticipationResponse> getParticipationsByWorkshop(Long workshopId, Pageable pageable);

    Page<ParticipationResponse> getParticipationsByFarmer(Long farmerId, Pageable pageable);
}
