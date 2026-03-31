package com.agriserve.repository;

import com.agriserve.entity.AdvisoryContent;
import com.agriserve.enums.ContentCategory;
import com.agriserve.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvisoryContentRepository extends JpaRepository<AdvisoryContent, Long> {

    Page<AdvisoryContent> findByCategory(ContentCategory category, Pageable pageable);

    Page<AdvisoryContent> findByStatus(Status status, Pageable pageable);

    Page<AdvisoryContent> findByCategoryAndStatus(ContentCategory category, Status status, Pageable pageable);

    @Query("SELECT ac FROM AdvisoryContent ac WHERE " +
           "LOWER(ac.title) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(ac.description) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<AdvisoryContent> search(@Param("q") String query, Pageable pageable);
}
