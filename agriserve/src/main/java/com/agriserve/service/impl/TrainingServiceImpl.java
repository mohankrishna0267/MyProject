package com.agriserve.service.impl;

import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.training.ParticipationRequest;
import com.agriserve.dto.training.ParticipationResponse;
import com.agriserve.dto.training.TrainingProgramRequest;
import com.agriserve.dto.training.TrainingProgramResponse;
import com.agriserve.dto.training.WorkshopRequest;
import com.agriserve.dto.training.WorkshopResponse;
import com.agriserve.entity.Farmer;
import com.agriserve.entity.Participation;
import com.agriserve.entity.TrainingProgram;
import com.agriserve.entity.User;
import com.agriserve.entity.Workshop;
import com.agriserve.exception.DuplicateResourceException;
import com.agriserve.exception.ResourceNotFoundException;
import com.agriserve.repository.FarmerRepository;
import com.agriserve.repository.ParticipationRepository;
import com.agriserve.repository.TrainingProgramRepository;
import com.agriserve.repository.UserRepository;
import com.agriserve.repository.WorkshopRepository;
import com.agriserve.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingProgramRepository programRepository;
    private final WorkshopRepository workshopRepository;
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final FarmerRepository farmerRepository;

    @Override
    public TrainingProgramResponse createProgram(TrainingProgramRequest request) {
        TrainingProgram program = TrainingProgram.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus())
                .build();

        program = programRepository.save(program);
        return mapToProgramResponse(program);
    }

    @Override
    public TrainingProgramResponse getProgramById(Long programId) {
        TrainingProgram program = programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("TrainingProgram", "id", programId));
        return mapToProgramResponse(program);
    }

    @Override
    public PagedResponse<TrainingProgramResponse> getAllPrograms(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TrainingProgram> programPage = programRepository.findAll(pageable);
        return PagedResponse.from(programPage.map(this::mapToProgramResponse));
    }

    @Override
    public WorkshopResponse createWorkshop(WorkshopRequest request) {
        TrainingProgram program = programRepository.findById(request.getProgramId())
                .orElseThrow(() -> new ResourceNotFoundException("TrainingProgram", "id", request.getProgramId()));

        User officer = userRepository.findById(request.getOfficerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getOfficerId()));

        Workshop workshop = Workshop.builder()
                .program(program)
                .officer(officer)
                .location(request.getLocation())
                .workshopDate(request.getWorkshopDate())
                .status(request.getStatus())
                .build();

        workshop = workshopRepository.save(workshop);
        return mapToWorkshopResponse(workshop);
    }

    @Override
    public WorkshopResponse getWorkshopById(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop", "id", workshopId));
        return mapToWorkshopResponse(workshop);
    }

    @Override
    public PagedResponse<WorkshopResponse> getWorkshopsByProgram(Long programId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Workshop> workshopPage = workshopRepository.findByProgram_ProgramId(programId, pageable);
        return PagedResponse.from(workshopPage.map(this::mapToWorkshopResponse));
    }

    @Override
    public ParticipationResponse registerFarmerForWorkshop(ParticipationRequest request) {
        if (participationRepository.existsByWorkshop_WorkshopIdAndFarmer_FarmerId(request.getWorkshopId(), request.getFarmerId())) {
            throw new DuplicateResourceException("Farmer is already registered for this workshop");
        }

        Workshop workshop = workshopRepository.findById(request.getWorkshopId())
                .orElseThrow(() -> new ResourceNotFoundException("Workshop", "id", request.getWorkshopId()));

        Farmer farmer = farmerRepository.findById(request.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer", "id", request.getFarmerId()));

        Participation participation = Participation.builder()
                .workshop(workshop)
                .farmer(farmer)
                .attendanceStatus(request.getAttendanceStatus())
                .build();

        participation = participationRepository.save(participation);
        return mapToParticipationResponse(participation);
    }

    @Override
    public PagedResponse<ParticipationResponse> getParticipationsByWorkshop(Long workshopId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Participation> pageResult = participationRepository.findByWorkshop_WorkshopId(workshopId, pageable);
        return PagedResponse.from(pageResult.map(this::mapToParticipationResponse));
    }


    /* -- Mappers -- */

    private TrainingProgramResponse mapToProgramResponse(TrainingProgram program) {
        return TrainingProgramResponse.builder()
                .programId(program.getProgramId())
                .title(program.getTitle())
                .description(program.getDescription())
                .startDate(program.getStartDate())
                .endDate(program.getEndDate())
                .status(program.getStatus())
                .createdAt(program.getCreatedAt())
                .updatedAt(program.getUpdatedAt())
                .build();
    }

    private WorkshopResponse mapToWorkshopResponse(Workshop workshop) {
        return WorkshopResponse.builder()
                .workshopId(workshop.getWorkshopId())
                .programId(workshop.getProgram().getProgramId())
                .programTitle(workshop.getProgram().getTitle())
                .officerId(workshop.getOfficer().getUserId())
                .officerName(workshop.getOfficer().getName())
                .location(workshop.getLocation())
                .workshopDate(workshop.getWorkshopDate())
                .status(workshop.getStatus())
                .createdAt(workshop.getCreatedAt())
                .build();
    }

    private ParticipationResponse mapToParticipationResponse(Participation participation) {
        return ParticipationResponse.builder()
                .participationId(participation.getParticipationId())
                .workshopId(participation.getWorkshop().getWorkshopId())
                .workshopLocation(participation.getWorkshop().getLocation())
                .farmerId(participation.getFarmer().getFarmerId())
                .farmerName(participation.getFarmer().getName())
                .attendanceStatus(participation.getAttendanceStatus())
                .feedback(participation.getFeedback())
                .createdAt(participation.getCreatedAt())
                .build();
    }
}
