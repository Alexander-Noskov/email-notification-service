package com.example.emailnotification;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@ActiveProfiles("test")
class KafkaTestcontainersConfig {
    @Bean
    @ServiceConnection
    KafkaContainer kafkaContainer() {
        return new KafkaContainer(DockerImageName.parse("apache/kafka-native:latest"));
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry, KafkaContainer kafka) {
        System.out.println(kafka.getBootstrapServers());
        registry.add("app.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }
}
