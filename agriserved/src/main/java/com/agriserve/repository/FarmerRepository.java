package com.agriserve.repository;

import com.agriserve.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    Optional<Farmer> findByContactInfo(String contactInfo);

    boolean existsByContactInfo(String contactInfo);
}