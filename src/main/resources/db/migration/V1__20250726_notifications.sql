CREATE TABLE IF NOT EXISTS notifications
(
    id                      UUID PRIMARY KEY,
    notification_request_id UUID                        NOT NULL,
    kafka_received_time     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    email_sent_time         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    error_message           VARCHAR(2000),
    service_name            VARCHAR(255)                NOT NULL,
    is_sent_successfully    BOOLEAN                     NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_notifications_kafka_received_time
    ON notifications (kafka_received_time);

CREATE INDEX IF NOT EXISTS idx_notifications_service_success
    ON notifications (service_name, is_sent_successfully);

CREATE INDEX IF NOT EXISTS idx_notifications_full_search
    ON notifications (service_name, is_sent_successfully, kafka_received_time);