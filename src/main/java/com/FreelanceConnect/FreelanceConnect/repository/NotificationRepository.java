package com.FreelanceConnect.FreelanceConnect.repository;

import com.FreelanceConnect.FreelanceConnect.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientEmailOrderByIdDesc(
            String recipientEmail
    );
}