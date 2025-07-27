package com.example.emailnotification.service;

import com.example.emailnotification.dto.NotificationRequest;
import com.example.emailnotification.entity.Notification;
import com.example.emailnotification.exception.NotFoundException;
import com.example.emailnotification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;

    public void processNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setNotificationRequestId(request.notificationId());
        notification.setKafkaReceivedTime(request.sendDate());
        notification.setServiceName(request.serviceName());

        try {
            emailService.sendEmail(request.address(), request.messageSubject(), request.messageBody());
            notification.setEmailSentTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
            notification.setSentSuccessfully(true);
            notificationRepository.save(notification);
        } catch (Exception e) {
            notification.setEmailSentTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
            notification.setSentSuccessfully(false);
            notification.setErrorMessage(e.getMessage());
            notificationRepository.save(notification);
            throw e;
        }
    }

    public Notification getNotificationById(UUID id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification with id " + id + " not found"));
    }

    @Override
    public List<Notification> getNotifications(Integer limit, Integer offset, Boolean isSuccess, String service) {
        return notificationRepository.findAllByParam(limit, offset, isSuccess, service);
    }
}
