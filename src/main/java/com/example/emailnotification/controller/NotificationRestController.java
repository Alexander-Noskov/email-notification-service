package com.example.emailnotification.controller;

import com.example.emailnotification.entity.Notification;
import com.example.emailnotification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationRestController {
    private final NotificationService notificationService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Notification getNotification(@PathVariable UUID id) {
        return notificationService.getNotificationById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> getNotifications(@RequestParam(defaultValue = "10") Integer limit,
                                               @RequestParam(defaultValue = "0") Integer offset,
                                               @RequestParam(name = "is_success", required = false) Boolean isSuccess,
                                               @RequestParam(required = false) String service) {
        return notificationService.getNotifications(limit, offset, isSuccess, service);
    }

}
