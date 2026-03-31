package com.agriserve.repository;

import com.agriserve.entity.Farmer;
import com.agriserve.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    Optional<Farmer> findByContactInfo(String contactInfo);

    boolean existsByContactInfo(String contactInfo);

    Page<Farmer> findByStatus(UserStatus status, Pageable pageable);

    Page<Farmer> findByCropTypeContainingIgnoreCase(String cropType, Pageable pageable);

    /** Full-text search on name, address, or crop type. */
    @Query("SELECT f FROM Farmer f WHERE " +
           "LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.cropType) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Farmer> searchFarmers(@Param("keyword") String keyword, Pageable pageable);
}
