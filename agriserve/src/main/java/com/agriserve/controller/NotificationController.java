package com.agriserve.controller;

import com.agriserve.dto.ApiResponse;
import com.agriserve.dto.PagedResponse;
import com.agriserve.dto.notification.NotificationResponse;
import com.agriserve.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ApiResponse<PagedResponse<NotificationResponse>> getMyNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // In a real app, authorize to make sure current user matches userId
        return ApiResponse.success(notificationService.getNotificationsByUser(userId, page, size));
    }

    @PatchMapping("/{notificationId}/read")
    public ApiResponse<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ApiResponse.success("Notification marked as read");
    }
}
