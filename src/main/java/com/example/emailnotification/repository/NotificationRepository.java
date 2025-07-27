package com.example.emailnotification.repository;

import com.example.emailnotification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    @Query("select n from Notification n " +
           "where (:success is null or n.isSentSuccessfully = :success) and (:service is null or n.serviceName = :service) " +
           "order by n.kafkaReceivedTime asc limit :limit offset :offset")
    List<Notification> findAllByParam(
            @Param("limit") Integer limit,
            @Param("offset") Integer offset,
            @Param("success") Boolean isSuccess,
            @Param("service") String service
    );
}
