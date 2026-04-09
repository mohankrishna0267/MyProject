package com.agriserve.service.impl;

import com.agriserve.dto.request.ParticipationRequest;
import com.agriserve.dto.request.TrainingProgramRequest;
import com.agriserve.dto.request.WorkshopRequest;
import com.agriserve.dto.response.ParticipationResponse;
import com.agriserve.dto.response.TrainingProgramResponse;
import com.agriserve.dto.response.WorkshopResponse;
import com.agriserve.entity.Farmer;
import com.agriserve.entity.Participation;
import com.agriserve.entity.TrainingProgram;
import com.agriserve.entity.User;
import com.agriserve.entity.Workshop;
import com.agriserve.entity.enums.Status;
import com.agriserve.exception.BusinessException;
import com.agriserve.exception.ResourceNotFoundException;
import com.agriserve.repository.FarmerRepository;
import com.agriserve.repository.ParticipationRepository;
import com.agriserve.repository.TrainingProgramRepository;
import com.agriserve.repository.UserRepository;
import com.agriserve.repository.WorkshopRepository;
import com.agriserve.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manages training programs, workshops, and farmer participation tracking.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingProgramRepository programRepository;
    private final WorkshopRepository workshopRepository;
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final FarmerRepository farmerRepository;

    // ─── Training Programs ────────────────────────────────────────────────────

    @Override
    @Transactional
    public TrainingProgramResponse createProgram(TrainingProgramRequest request) {
        TrainingProgram program = TrainingProgram.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus() != null ? request.getStatus() : Status.DRAFT)
                .build();
        return TrainingProgramResponse.from(programRepository.save(program));
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingProgramResponse getProgramById(Long programId) {
        return TrainingProgramResponse.from(findProgramOrThrow(programId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingProgramResponse> getAllPrograms(Status status, Pageable pageable) {
        if (status != null) {
            return programRepository.findAllByStatus(status, pageable).map(TrainingProgramResponse::from);
        }
        return programRepository.findAll(pageable).map(TrainingProgramResponse::from);
    }

    @Override
    @Transactional
    public TrainingProgramResponse updateProgram(Long programId, TrainingProgramRequest request) {
        TrainingProgram program = findProgramOrThrow(programId);
        program.setTitle(request.getTitle());
        program.setDescription(request.getDescription());
        program.setStartDate(request.getStartDate());
        program.setEndDate(request.getEndDate());
        if (request.getStatus() != null) program.setStatus(request.getStatus());
        return TrainingProgramResponse.from(programRepository.save(program));
    }

    @Override
    @Transactional
    public void deleteProgram(Long programId) {
        TrainingProgram program = findProgramOrThrow(programId);
        programRepository.delete(program);
    }

    // ─── Workshops ────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public WorkshopResponse createWorkshop(WorkshopRequest request) {
        TrainingProgram program = findProgramOrThrow(request.getProgramId());
        User officer = userRepository.findById(request.getOfficerId())
                .orElseThrow(() -> new ResourceNotFoundException("User (Officer)", "id", request.getOfficerId()));

        Workshop workshop = Workshop.builder()
                .program(program)
                .officer(officer)
                .location(request.getLocation())
                .workshopDate(request.getWorkshopDate())
                .status(Status.PENDING)
                .build();
        return WorkshopResponse.from(workshopRepository.save(workshop));
    }

    @Override
    @Transactional(readOnly = true)
    public WorkshopResponse getWorkshopById(Long workshopId) {
        return WorkshopResponse.from(findWorkshopOrThrow(workshopId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkshopResponse> getWorkshopsByProgram(Long programId, Pageable pageable) {
        findProgramOrThrow(programId);
        return workshopRepository.findAllByProgram_ProgramId(programId, pageable).map(WorkshopResponse::from);
    }

    @Override
    @Transactional
    public WorkshopResponse updateWorkshopStatus(Long workshopId, Status status) {
        Workshop workshop = findWorkshopOrThrow(workshopId);
        workshop.setStatus(status);
        return WorkshopResponse.from(workshopRepository.save(workshop));
    }

    // ─── Participation ────────────────────────────────────────────────────────

    @Override
    @Transactional
    public ParticipationResponse registerParticipation(ParticipationRequest request) {
        Workshop workshop = findWorkshopOrThrow(request.getWorkshopId());
        Farmer farmer = farmerRepository.findById(request.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer", "id", request.getFarmerId()));

        // Enforce uniqueness: one registration per farmer per workshop
        participationRepository.findByWorkshop_WorkshopIdAndFarmer_FarmerId(
                request.getWorkshopId(), request.getFarmerId())
                .ifPresent(p -> {
                    throw new BusinessException("Farmer is already registered for this workshop");
                });

        Participation participation = Participation.builder()
                .workshop(workshop)
                .farmer(farmer)
                .attendanceStatus(request.getAttendanceStatus())
                .feedback(request.getFeedback())
                .build();
        return ParticipationResponse.from(participationRepository.save(participation));
    }

    @Override
    @Transactional
    public ParticipationResponse updateParticipation(Long participationId, ParticipationRequest request) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new ResourceNotFoundException("Participation", "id", participationId));
        participation.setAttendanceStatus(request.getAttendanceStatus());
        participation.setFeedback(request.getFeedback());
        return ParticipationResponse.from(participationRepository.save(participation));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ParticipationResponse> getParticipationsByWorkshop(Long workshopId, Pageable pageable) {
        return participationRepository.findAllByWorkshop_WorkshopId(workshopId, pageable)
                .map(ParticipationResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ParticipationResponse> getParticipationsByFarmer(Long farmerId, Pageable pageable) {
        return participationRepository.findAllByFarmer_FarmerId(farmerId, pageable)
                .map(ParticipationResponse::from);
    }

    // ─── Private Helpers ─────────────────────────────────────────────────────

    private TrainingProgram findProgramOrThrow(Long id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TrainingProgram", "id", id));
    }

    private Workshop findWorkshopOrThrow(Long id) {
        return workshopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop", "id", id));
    }
}
