package com.FreelanceConnect.FreelanceConnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "proposals")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    private String freelancerEmail;

    @Column(length = 2000)
    private String message;

    private double bidAmount;

    private String status;

    public Proposal() {
    }

    public Proposal(Long projectId, String freelancerEmail,
                    String message, double bidAmount, String status) {
        this.projectId = projectId;
        this.freelancerEmail = freelancerEmail;
        this.message = message;
        this.bidAmount = bidAmount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getFreelancerEmail() {
        return freelancerEmail;
    }

    public void setFreelancerEmail(String freelancerEmail) {
        this.freelancerEmail = freelancerEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}