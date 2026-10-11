package com.FreelanceConnect.FreelanceConnect.repository;

import com.FreelanceConnect.FreelanceConnect.entity.FreelancerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreelancerProfileRepository
        extends JpaRepository<FreelancerProfile, Long> {

    Optional<FreelancerProfile> findByEmail(String email);
}