package com.agriserve.service.impl;

import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.notification.NotificationResponse;
import com.agriserve.entity.Notification;
import com.agriserve.entity.User;
import com.agriserve.enums.NotificationCategory;
import com.agriserve.enums.NotificationStatus;
import com.agriserve.exception.ResourceNotFoundException;
import com.agriserve.repository.NotificationRepository;
import com.agriserve.repository.UserRepository;
import com.agriserve.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public void createNotification(Long userId, Long entityId, String message, NotificationCategory category) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Notification notification = Notification.builder()
                .user(user)
                .entityId(entityId)
                .message(message)
                .category(category)
                .status(NotificationStatus.UNREAD)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public PagedResponse<NotificationResponse> getNotificationsByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notificationPage = notificationRepository.findByUser_UserId(userId, pageable);
        return PagedResponse.from(notificationPage.map(this::mapToNotificationResponse));
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));

        notification.setStatus(NotificationStatus.READ);
        notificationRepository.save(notification);
    }

    private NotificationResponse mapToNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .userId(notification.getUser().getUserId())
                .entityId(notification.getEntityId())
                .message(notification.getMessage())
                .category(notification.getCategory())
                .status(notification.getStatus())
                .createdDate(notification.getCreatedDate())
                .build();
    }
}
