package com.FreelanceConnect.FreelanceConnect.controller;

import com.FreelanceConnect.FreelanceConnect.entity.Payment;
import com.FreelanceConnect.FreelanceConnect.entity.Project;
import com.FreelanceConnect.FreelanceConnect.entity.Proposal;

import com.FreelanceConnect.FreelanceConnect.repository.PaymentRepository;
import com.FreelanceConnect.FreelanceConnect.repository.ProjectRepository;
import com.FreelanceConnect.FreelanceConnect.repository.ProposalRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final ProjectRepository projectRepository;
    private final ProposalRepository proposalRepository;

    public PaymentController(
            PaymentRepository paymentRepository,
            ProjectRepository projectRepository,
            ProposalRepository proposalRepository) {

        this.paymentRepository = paymentRepository;
        this.projectRepository = projectRepository;
        this.proposalRepository = proposalRepository;
    }

    // CREATE A PENDING PAYMENT

    @PostMapping("/project/{projectId}")
    public Payment createPayment(
            @PathVariable Long projectId,
            Authentication authentication) {

        String clientEmail = authentication.getName();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Project not found"));

        if (!clientEmail.equals(project.getClientEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only the project owner can initiate payment");
        }

        if (project.getFreelancerEmail() == null ||
                project.getFreelancerEmail().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No freelancer has been selected for this project");
        }

        // ALLOW PAYMENT FOR IN-PROGRESS AND COMPLETED PROJECTS

        if (!"IN_PROGRESS".equals(project.getStatus()) &&
                !"COMPLETED".equals(project.getStatus())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment can only be initiated for an in-progress or completed project");
        }

        Payment existingPayment =
                paymentRepository.findByProjectId(projectId).orElse(null);

        if (existingPayment != null) {
            return existingPayment;
        }

        Proposal acceptedProposal =
                proposalRepository
                        .findByProjectIdAndFreelancerEmailAndStatus(
                                projectId,
                                project.getFreelancerEmail(),
                                "ACCEPTED")
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Accepted proposal not found"));

        if (acceptedProposal.getBidAmount() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid proposal amount");
        }

        Payment payment = new Payment();

        payment.setProjectId(projectId);
        payment.setClientEmail(clientEmail);
        payment.setFreelancerEmail(project.getFreelancerEmail());

        payment.setAmount(
                BigDecimal.valueOf(acceptedProposal.getBidAmount())
                        .setScale(2, java.math.RoundingMode.HALF_UP));

        payment.setStatus("PENDING");

        return paymentRepository.save(payment);
    }

    // SIMULATE A SUCCESSFUL PAYMENT
    // This does not transfer real money.

    @PutMapping("/{paymentId}/demo-confirm")
    public Payment confirmDemoPayment(
            @PathVariable Long paymentId,
            Authentication authentication) {

        String clientEmail = authentication.getName();

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Payment not found"));

        if (!clientEmail.equals(payment.getClientEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only the client can confirm this payment");
        }

        if ("PAID".equals(payment.getStatus())) {
            return payment;
        }

        if (!"PENDING".equals(payment.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment is not pending");
        }

        payment.setStatus("PAID");
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    // VIEW PAYMENTS AS A CLIENT

    @GetMapping("/my-client-payments")
    public List<Payment> getClientPayments(
            Authentication authentication) {

        return paymentRepository
                .findByClientEmailOrderByIdDesc(
                        authentication.getName());
    }

    // VIEW PAYMENTS AS A FREELANCER

    @GetMapping("/my-freelancer-payments")
    public List<Payment> getFreelancerPayments(
            Authentication authentication) {

        return paymentRepository
                .findByFreelancerEmailOrderByIdDesc(
                        authentication.getName());
    }
}