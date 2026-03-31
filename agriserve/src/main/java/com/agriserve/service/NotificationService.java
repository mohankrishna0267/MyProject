package com.agriserve.service;

import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.notification.NotificationResponse;
import com.agriserve.enums.NotificationCategory;

public interface NotificationService {

    void createNotification(Long userId, Long entityId, String message, NotificationCategory category);

    PagedResponse<NotificationResponse> getNotificationsByUser(Long userId, int page, int size);

    void markAsRead(Long notificationId);
}
