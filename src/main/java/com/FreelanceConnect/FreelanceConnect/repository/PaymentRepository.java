package com.FreelanceConnect.FreelanceConnect.repository;

import com.FreelanceConnect.FreelanceConnect.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByClientEmailOrderByIdDesc(String clientEmail);

    List<Payment> findByFreelancerEmailOrderByIdDesc(String freelancerEmail);

    Optional<Payment> findByProjectId(Long projectId);
}