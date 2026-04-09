package com.agriserve.repository;

import com.agriserve.entity.Farmer;
import com.agriserve.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    Page<Farmer> findAllByStatus(Status status, Pageable pageable);

    @Query("SELECT f FROM Farmer f WHERE " +
           "(:name IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:cropType IS NULL OR LOWER(f.cropType) LIKE LOWER(CONCAT('%', :cropType, '%'))) AND " +
           "(:status IS NULL OR f.status = :status)")
    Page<Farmer> searchFarmers(
        @Param("name") String name,
        @Param("cropType") String cropType,
        @Param("status") Status status,
        Pageable pageable
    );
}
