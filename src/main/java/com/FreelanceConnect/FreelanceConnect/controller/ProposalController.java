package com.FreelanceConnect.FreelanceConnect.controller;

import com.FreelanceConnect.FreelanceConnect.entity.Proposal;
import com.FreelanceConnect.FreelanceConnect.entity.Project;
import com.FreelanceConnect.FreelanceConnect.model.Notification;
import com.FreelanceConnect.FreelanceConnect.repository.NotificationRepository;
import com.FreelanceConnect.FreelanceConnect.repository.ProposalRepository;
import com.FreelanceConnect.FreelanceConnect.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proposals")
public class ProposalController {

    private final ProposalRepository proposalRepository;
    private final ProjectRepository projectRepository;
    private final NotificationRepository notificationRepository;

    public ProposalController(
            ProposalRepository proposalRepository,
            ProjectRepository projectRepository,
            NotificationRepository notificationRepository) {

        this.proposalRepository = proposalRepository;
        this.projectRepository = projectRepository;
        this.notificationRepository = notificationRepository;
    }

    // CREATE PROPOSAL
    @PostMapping
    public Proposal createProposal(
            @RequestBody Proposal proposal) {

        if (proposal.getStatus() == null ||
                proposal.getStatus().isEmpty()) {

            proposal.setStatus("PENDING");
        }

        return proposalRepository.save(proposal);
    }

    // GET ALL PROPOSALS
    @GetMapping
    public List<Proposal> getAllProposals() {

        return proposalRepository.findAll();
    }

    // GET FREELANCER PROPOSALS
    @GetMapping("/freelancer/{email}")
    public List<Proposal> getFreelancerProposals(
            @PathVariable String email) {

        return proposalRepository
                .findByFreelancerEmail(email);
    }

    // GET PROPOSALS FOR A PROJECT
    @GetMapping("/project/{projectId}")
    public List<Proposal> getProjectProposals(
            @PathVariable Long projectId) {

        return proposalRepository
                .findByProjectId(projectId);
    }

    // ACCEPT PROPOSAL
    @PutMapping("/{id}/accept")
    public Proposal acceptProposal(
            @PathVariable Long id) {

        Proposal proposal =
                proposalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Proposal not found"));

        proposal.setStatus("ACCEPTED");

        Project project =
                projectRepository.findById(
                                proposal.getProjectId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Project not found"));

        // Save the hired freelancer's email
        project.setFreelancerEmail(
                proposal.getFreelancerEmail()
        );

        // Change project status
        project.setStatus("IN_PROGRESS");

        projectRepository.save(project);

        // CREATE NOTIFICATION FOR FREELANCER
        Notification notification = new Notification();

        notification.setRecipientEmail(
                proposal.getFreelancerEmail()
        );

        notification.setMessage(
                "Your proposal for project #" +
                        project.getId() +
                        " has been accepted."
        );

        notificationRepository.save(notification);

        return proposalRepository.save(proposal);
    }
}