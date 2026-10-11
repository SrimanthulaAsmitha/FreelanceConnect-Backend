package com.FreelanceConnect.FreelanceConnect.controller;

import com.FreelanceConnect.FreelanceConnect.entity.Message;
import com.FreelanceConnect.FreelanceConnect.repository.MessageRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository messageRepository;

    public MessageController(
            MessageRepository messageRepository) {

        this.messageRepository =
                messageRepository;
    }

    // ================= GET PROJECT MESSAGES =================

    @GetMapping("/project/{projectId}")
    public List<Message> getProjectMessages(
            @PathVariable Long projectId) {

        return messageRepository
                .findByProjectIdOrderByIdAsc(projectId);
    }


    // ================= SEND MESSAGE =================

    @PostMapping
    public Message sendMessage(
            @RequestBody Message message) {

        return messageRepository.save(message);
    }
}