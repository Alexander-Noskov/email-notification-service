package com.example.emailnotification.service;

import com.example.emailnotification.dto.NotificationRequest;
import com.example.emailnotification.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void processNotification(NotificationRequest request);

    Notification getNotificationById(UUID id);

    List<Notification> getNotifications(Integer limit, Integer offset, Boolean isSuccess, String service);
}
