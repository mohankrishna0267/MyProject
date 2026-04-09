package com.agriserve.service.impl;

import com.agriserve.dto.request.NotificationRequest;
import com.agriserve.dto.response.NotificationResponse;
import com.agriserve.entity.Notification;
import com.agriserve.entity.User;
import com.agriserve.entity.enums.Status;
import com.agriserve.exception.ResourceNotFoundException;
import com.agriserve.repository.NotificationRepository;
import com.agriserve.repository.UserRepository;
import com.agriserve.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manages system notifications sent to users.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public NotificationResponse sendNotification(NotificationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        Notification notification = Notification.builder()
                .user(user)
                .entityId(request.getEntityId())
                .message(request.getMessage())
                .category(request.getCategory())
                .status(Status.PENDING)
                .build();
        return NotificationResponse.from(notificationRepository.save(notification));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findAllByUser_UserId(userId, pageable).map(NotificationResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUnreadNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findAllByUser_UserIdAndStatus(userId, Status.PENDING, pageable)
                .map(NotificationResponse::from);
    }

    @Override
    @Transactional
    public int markAllAsRead(Long userId) {
        return notificationRepository.markAllAsReadForUser(userId);
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
        notification.setStatus(Status.ACTIVE);
        return NotificationResponse.from(notificationRepository.save(notification));
    }
}
