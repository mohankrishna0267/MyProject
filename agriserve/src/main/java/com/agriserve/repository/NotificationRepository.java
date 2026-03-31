package com.agriserve.repository;

import com.agriserve.entity.Notification;
import com.agriserve.enums.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUser_UserId(Long userId, Pageable pageable);

    Page<Notification> findByUser_UserIdAndStatus(Long userId, NotificationStatus status, Pageable pageable);

    long countByUser_UserIdAndStatus(Long userId, NotificationStatus status);
}
