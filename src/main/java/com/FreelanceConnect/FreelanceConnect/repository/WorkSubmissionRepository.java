package com.FreelanceConnect.FreelanceConnect.repository;

import com.FreelanceConnect.FreelanceConnect.entity.WorkSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkSubmissionRepository
        extends JpaRepository<WorkSubmission, Long> {

    List<WorkSubmission> findByProjectId(Long projectId);

    List<WorkSubmission> findByFreelancerEmail(String freelancerEmail);
}