package com.FreelanceConnect.FreelanceConnect.controller;

import com.FreelanceConnect.FreelanceConnect.model.Notification;
import com.FreelanceConnect.FreelanceConnect.repository.NotificationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(
            NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping
    public List<Notification> getNotifications(
            @RequestParam String email) {

        return notificationRepository
                .findByRecipientEmailOrderByIdDesc(email);
    }

    @PostMapping
    public Notification createNotification(
            @RequestBody Notification notification) {

        return notificationRepository.save(notification);
    }

    @PutMapping("/{id}/read")
    public Notification markAsRead(
            @PathVariable Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"));

        notification.setReadStatus(true);

        return notificationRepository.save(notification);
    }
}