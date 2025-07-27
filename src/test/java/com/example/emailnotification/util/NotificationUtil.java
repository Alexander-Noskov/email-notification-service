package com.example.emailnotification.util;

import com.example.emailnotification.entity.Notification;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public final class NotificationUtil {
    public static Notification getNotification() {
        return Notification.builder()
                .notificationRequestId(UUID.randomUUID())
                .emailSentTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                .isSentSuccessfully(true)
                .kafkaReceivedTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                .serviceName("Example Service")
                .build();
    }

    public static Notification getErrorNotification() {
        return Notification.builder()
                .notificationRequestId(UUID.randomUUID())
                .emailSentTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                .isSentSuccessfully(false)
                .kafkaReceivedTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                .serviceName("Example Service")
                .errorMessage("Error message")
                .build();
    }
}
