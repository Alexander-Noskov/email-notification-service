package com.example.emailnotification.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationRequest(
        String address,
        UUID notificationId,
        String serviceName,
        String messageSubject,
        String messageBody,
        LocalDateTime sendDate
) {
}
