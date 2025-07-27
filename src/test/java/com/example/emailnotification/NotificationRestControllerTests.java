package com.example.emailnotification;

import com.example.emailnotification.dto.ErrorResponse;
import com.example.emailnotification.entity.Notification;
import com.example.emailnotification.repository.NotificationRepository;
import com.example.emailnotification.util.NotificationUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

@Import(PostgresTestcontainersConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationRestControllerTests {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private NotificationRepository notificationRepository;

    @AfterEach
    void cleanUp() {
        notificationRepository.deleteAll();
    }

    @Test
    @DisplayName("Получение нотификации по uuid")
    void testGetNotificationByUuid() {
        Notification notification = NotificationUtil.getNotification();
        notificationRepository.save(notification);

        WebTestClient.ResponseSpec result = webClient.get()
                .uri("api/v1/notifications/" + notification.getId())
                .exchange();
        result.expectStatus().isOk()
                .expectBody(Notification.class)
                .isEqualTo(notification);
    }

    @Test
    @DisplayName("Получение нотификации по несуществующему uuid")
    void testGetNotificationByNonExistUuid() {
        WebTestClient.ResponseSpec result = webClient.get()
                .uri("api/v1/notifications/" + UUID.randomUUID())
                .exchange();
        result.expectStatus().isNotFound()
                .expectBody(ErrorResponse.class);
    }

    @Test
    @DisplayName("Получение нотификаций")
    void testGetNotifications() {
        Notification notification1 = NotificationUtil.getNotification();
        Notification notification2 = NotificationUtil.getNotification();

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        WebTestClient.ResponseSpec result = webClient.get()
                .uri("api/v1/notifications")
                .exchange();
        result.expectStatus().isOk()
                .expectBodyList(Notification.class)
                .isEqualTo(List.of(notification1, notification2));
    }

    @Test
    @DisplayName("Получение неотправленных нотификаций")
    void testGetErrorNotifications() {
        Notification notification1 = NotificationUtil.getErrorNotification();
        Notification notification2 = NotificationUtil.getNotification();

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        WebTestClient.ResponseSpec result = webClient.get()
                .uri("api/v1/notifications?is_success=false")
                .exchange();
        result.expectStatus().isOk()
                .expectBodyList(Notification.class)
                .isEqualTo(List.of(notification1));
    }

    @Test
    @DisplayName("Получение нотификаций с указанным именем сервиса")
    void testGetNotificationsByServiceName() {
        Notification notification1 = NotificationUtil.getNotification();
        Notification notification2 = NotificationUtil.getNotification();
        notification2.setServiceName("test");

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        WebTestClient.ResponseSpec result = webClient.get()
                .uri("api/v1/notifications?service=" + notification1.getServiceName())
                .exchange();
        result.expectStatus().isOk()
                .expectBodyList(Notification.class)
                .isEqualTo(List.of(notification1));
    }
}
