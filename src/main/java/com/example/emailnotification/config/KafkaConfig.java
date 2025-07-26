package com.example.emailnotification.config;

import com.example.emailnotification.dto.NotificationRequest;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {
    @Value("${app.kafka.topic}")
    private String topic;

    @Value("${app.kafka.dlq-topic}")
    private String dlqTopic;

    @Value("${app.kafka.partitions}")
    private int partitions;

    @Value("${app.kafka.replication-factor}")
    private short replicationFactor;

    @Bean
    public NewTopic createTopic() {
        return TopicBuilder.name(topic)
                .partitions(partitions)
                .replicas(replicationFactor)
                .build();
    }

    @Bean
    public NewTopic createDLQTopic() {
        return TopicBuilder.name(dlqTopic)
                .partitions(partitions)
                .replicas(replicationFactor)
                .build();
    }

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<String, NotificationRequest> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, exception) -> new TopicPartition(dlqTopic, 0));
    }

    @Bean
    public KafkaTemplate<String, NotificationRequest> kafkaTemplate(ProducerFactory<String, NotificationRequest> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public CommonErrorHandler kafkaErrorHandler(DeadLetterPublishingRecoverer recoverer) {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(100L, 3));
        errorHandler.setCommitRecovered(true);
        return errorHandler;
    }
}
