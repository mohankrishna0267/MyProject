package com.agriserve.repository;

import com.agriserve.entity.Participation;
import com.agriserve.enums.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Optional<Participation> findByWorkshop_WorkshopIdAndFarmer_FarmerId(Long workshopId, Long farmerId);

    boolean existsByWorkshop_WorkshopIdAndFarmer_FarmerId(Long workshopId, Long farmerId);

    Page<Participation> findByWorkshop_WorkshopId(Long workshopId, Pageable pageable);

    Page<Participation> findByFarmer_FarmerId(Long farmerId, Pageable pageable);

    Page<Participation> findByAttendanceStatus(AttendanceStatus attendanceStatus, Pageable pageable);
}
