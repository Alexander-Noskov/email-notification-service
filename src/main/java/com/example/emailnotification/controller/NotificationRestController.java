package com.example.emailnotification.controller;

import com.example.emailnotification.entity.Notification;
import com.example.emailnotification.service.NotificationService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@Validated
public class NotificationRestController {
    private final NotificationService notificationService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Notification getNotification(@PathVariable UUID id) {
        return notificationService.getNotificationById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> getNotifications(@RequestParam(defaultValue = "10") @Positive Integer limit,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer offset,
                                               @RequestParam(name = "is_success", required = false) Boolean isSuccess,
                                               @RequestParam(required = false) String service) {
        return notificationService.getNotifications(limit, offset, isSuccess, service);
    }
}
