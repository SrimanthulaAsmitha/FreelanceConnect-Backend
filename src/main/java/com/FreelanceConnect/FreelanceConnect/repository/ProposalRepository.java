package com.FreelanceConnect.FreelanceConnect.repository;

import com.FreelanceConnect.FreelanceConnect.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProposalRepository
        extends JpaRepository<Proposal, Long> {

    List<Proposal> findByFreelancerEmail(String freelancerEmail);

    List<Proposal> findByProjectId(Long projectId);

    Optional<Proposal> findByProjectIdAndFreelancerEmailAndStatus(
            Long projectId,
            String freelancerEmail,
            String status
    );
}