package com.example.emailnotification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "notification_request_id", nullable = false)
    private UUID notificationRequestId;

    @Column(name = "kafka_received_time", nullable = false)
    private LocalDateTime kafkaReceivedTime;

    @Column(name = "email_sent_time", nullable = false)
    private LocalDateTime emailSentTime;

    @Column(name = "error_message", length = 2000)
    private String errorMessage;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "is_sent_successfully", nullable = false)
    private boolean isSentSuccessfully;
}
