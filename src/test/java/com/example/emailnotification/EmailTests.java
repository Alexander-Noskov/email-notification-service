package com.example.emailnotification;

import com.example.emailnotification.dto.NotificationRequest;
import com.example.emailnotification.entity.Notification;
import com.example.emailnotification.repository.NotificationRepository;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Import({PostgresTestcontainersConfig.class, KafkaTestcontainersConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EmailTests {

    @Autowired
    private KafkaTemplate<String, NotificationRequest> kafkaTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("email@mail.com", "pwd"))
            .withPerMethodLifecycle(false);

    @AfterEach
    public void cleanup() {
        notificationRepository.deleteAll();
    }

    @Test
    public void emailSentSuccessfully() {
        NotificationRequest n = new NotificationRequest(
                "abc@mail.com",
                UUID.randomUUID(),
                "Service 1",
                "Subject",
                "Body",
                LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

        kafkaTemplate.send("notifications", n);

        await().pollInterval(Duration.ofSeconds(5)).atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            assertEquals(1, notificationRepository.count());
            Notification notification = notificationRepository.findAll().getFirst();
            assertEquals(n.notificationId(), notification.getNotificationRequestId());
            assertEquals(n.sendDate(), notification.getKafkaReceivedTime());
            assertEquals(n.serviceName(), notification.getServiceName());

            assertEquals(1, greenMail.getReceivedMessages().length);
            MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
            assertEquals(n.messageBody(), GreenMailUtil.getBody(receivedMessage));
        });
    }
}
