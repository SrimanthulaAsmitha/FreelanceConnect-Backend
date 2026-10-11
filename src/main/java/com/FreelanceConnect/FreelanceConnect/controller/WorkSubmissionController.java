package com.FreelanceConnect.FreelanceConnect.controller;

import com.FreelanceConnect.FreelanceConnect.entity.Project;
import com.FreelanceConnect.FreelanceConnect.entity.WorkSubmission;
import com.FreelanceConnect.FreelanceConnect.repository.ProjectRepository;
import com.FreelanceConnect.FreelanceConnect.repository.WorkSubmissionRepository;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

@RestController
@RequestMapping("/api/work-submissions")
public class WorkSubmissionController {

    private final WorkSubmissionRepository workSubmissionRepository;
    private final ProjectRepository projectRepository;

    private final String uploadDirectory = "uploads/";

    public WorkSubmissionController(
            WorkSubmissionRepository workSubmissionRepository,
            ProjectRepository projectRepository) {

        this.workSubmissionRepository =
                workSubmissionRepository;

        this.projectRepository =
                projectRepository;
    }


    // =====================================================
    // UPLOAD WORK
    // =====================================================

    @PostMapping("/upload")
    public WorkSubmission uploadWork(
            @RequestParam Long projectId,
            @RequestParam String description,
            @RequestParam MultipartFile file,
            Authentication authentication)
            throws IOException {

        String loggedInEmail =
                authentication.getName();

        Project project =
                projectRepository.findById(projectId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Project not found"));

        if (!"IN_PROGRESS".equals(
                project.getStatus())) {

            throw new RuntimeException(
                    "Work can only be submitted for an IN_PROGRESS project"
            );
        }

        if (project.getFreelancerEmail() == null ||
                !project.getFreelancerEmail()
                        .equalsIgnoreCase(loggedInEmail)) {

            throw new RuntimeException(
                    "You are not the assigned freelancer for this project"
            );
        }

        if (file == null || file.isEmpty()) {

            throw new RuntimeException(
                    "Please select a file"
            );
        }

        Path uploadPath =
                Paths.get(uploadDirectory);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName =
                file.getOriginalFilename();

        String fileName =
                System.currentTimeMillis()
                        + "_"
                        + originalFileName;

        Path filePath =
                uploadPath.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath
        );

        WorkSubmission submission =
                new WorkSubmission();

        submission.setProjectId(projectId);

        submission.setFreelancerEmail(
                loggedInEmail
        );

        submission.setDescription(
                description
        );

        submission.setFileName(
                originalFileName
        );

        submission.setFileType(
                file.getContentType()
        );

        submission.setFilePath(
                filePath.toString()
        );

        submission.setStatus(
                "SUBMITTED"
        );

        WorkSubmission savedSubmission =
                workSubmissionRepository.save(
                        submission
                );

        project.setStatus(
                "WORK_COMPLETED"
        );

        projectRepository.save(project);

        return savedSubmission;
    }


    // =====================================================
    // GET SUBMITTED WORK FOR PROJECT
    // =====================================================

    @GetMapping("/project/{projectId}")
    public List<WorkSubmission> getProjectSubmissions(
            @PathVariable Long projectId) {

        return workSubmissionRepository
                .findByProjectId(projectId);
    }


    // =====================================================
    // GET SUBMITTED WORK FOR FREELANCER
    // =====================================================

    @GetMapping("/freelancer/{email}")
    public List<WorkSubmission> getFreelancerSubmissions(
            @PathVariable String email) {

        return workSubmissionRepository
                .findByFreelancerEmail(email);
    }


    // =====================================================
    // DOWNLOAD SUBMITTED WORK
    // =====================================================

    @GetMapping("/download/{submissionId}")
    public ResponseEntity<Resource> downloadWork(
            @PathVariable Long submissionId) {

        try {

            WorkSubmission submission =
                    workSubmissionRepository
                            .findById(submissionId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Submission not found"
                                    )
                            );


            // Get saved file path

            Path filePath =
                    Paths.get(
                            submission.getFilePath()
                    );


            // Check if file exists

            if (!Files.exists(filePath)) {

                System.out.println(
                        "FILE NOT FOUND: "
                                + filePath
                );

                return ResponseEntity
                        .notFound()
                        .build();
            }


            // Create resource

            Resource resource =
                    new FileSystemResource(
                            filePath.toFile()
                    );


            // Return file

            return ResponseEntity
                    .ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                                    submission.getFileName() +
                                    "\""
                    )
                    .header(
                            HttpHeaders.CONTENT_TYPE,
                            submission.getFileType() != null
                                    ? submission.getFileType()
                                    : "application/octet-stream"
                    )
                    .body(resource);


        } catch (Exception error) {

            error.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }
}