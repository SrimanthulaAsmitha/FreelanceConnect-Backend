package com.FreelanceConnect.FreelanceConnect.repository;

import com.FreelanceConnect.FreelanceConnect.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Find all projects posted by a particular client
    List<Project> findByClientEmail(String clientEmail);

    // Find all projects assigned to a particular freelancer
    List<Project> findByFreelancerEmail(String freelancerEmail);
}