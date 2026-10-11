package com.FreelanceConnect.FreelanceConnect.controller;

import com.FreelanceConnect.FreelanceConnect.entity.Project;
import com.FreelanceConnect.FreelanceConnect.repository.ProjectRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    // ================= CREATE PROJECT =================

    @PostMapping
    public Project createProject(
            @RequestBody Project project,
            Authentication authentication) {

        if (project.getStatus() == null ||
                project.getStatus().isEmpty()) {

            project.setStatus("OPEN");
        }

        String clientEmail =
                authentication.getName();

        project.setClientEmail(clientEmail);

        return projectRepository.save(project);
    }


    // ================= GET ALL PROJECTS =================

    @GetMapping
    public List<Project> getAllProjects() {

        return projectRepository.findAll();
    }


    // ================= GET MY PROJECTS =================
    // Freelancer projects

    @GetMapping("/my-projects")
    public List<Project> getMyProjects(
            Authentication authentication) {

        String freelancerEmail =
                authentication.getName();

        System.out.println(
                "MY PROJECTS - FREELANCER: " +
                        freelancerEmail
        );

        List<Project> projects =
                projectRepository
                        .findByFreelancerEmail(freelancerEmail);

        System.out.println(
                "MY PROJECTS - COUNT: " +
                        projects.size()
        );

        return projects;
    }


    // ================= GET CLIENT PROJECTS =================
    // Projects posted by the logged-in client

    @GetMapping("/client-projects")
    public List<Project> getClientProjects(
            Authentication authentication) {

        String clientEmail =
                authentication.getName();

        System.out.println(
                "CLIENT PROJECTS: " +
                        clientEmail
        );

        List<Project> projects =
                projectRepository
                        .findByClientEmail(clientEmail);

        System.out.println(
                "CLIENT PROJECTS - COUNT: " +
                        projects.size()
        );

        return projects;
    }


    // ================= MARK WORK COMPLETED =================

    @PutMapping("/{id}/work-completed")
    public Project markWorkCompleted(
            @PathVariable Long id,
            Authentication authentication) {

        System.out.println(
                "MARK WORK COMPLETED REQUEST"
        );

        System.out.println(
                "Project ID: " + id
        );

        System.out.println(
                "Logged in user: " +
                        authentication.getName()
        );


        Project project =
                projectRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Project not found"
                                )
                        );


        System.out.println(
                "Current project status: " +
                        project.getStatus()
        );


        project.setStatus(
                "WORK_COMPLETED"
        );


        Project savedProject =
                projectRepository.save(project);


        System.out.println(
                "New project status: " +
                        savedProject.getStatus()
        );


        return savedProject;
    }


    // ================= CLIENT APPROVES PROJECT =================

    @PutMapping("/{id}/complete")
    public Project completeProject(
            @PathVariable Long id,
            Authentication authentication) {

        System.out.println(
                "APPROVE PROJECT REQUEST"
        );

        System.out.println(
                "Project ID: " + id
        );

        System.out.println(
                "Logged in user: " +
                        authentication.getName()
        );


        Project project =
                projectRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Project not found"
                                )
                        );


        System.out.println(
                "Current project status: " +
                        project.getStatus()
        );


        project.setStatus(
                "COMPLETED"
        );


        Project savedProject =
                projectRepository.save(project);


        System.out.println(
                "New project status: " +
                        savedProject.getStatus()
        );


        return savedProject;
    }
}