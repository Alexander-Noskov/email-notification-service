package com.example.emailnotification.service;

import com.example.emailnotification.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaListener {
    private final NotificationService notificationService;

    @KafkaListener(
            id = "${app.kafka.listener-id}",
            topics = "${app.kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(NotificationRequest request, Acknowledgment ack) {
        try {
            notificationService.processNotification(request);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения: {}", request, e);
            throw e;
        }
    }
}
