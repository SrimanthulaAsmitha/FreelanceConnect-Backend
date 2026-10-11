package com.FreelanceConnect.FreelanceConnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "freelancer_profiles")
public class FreelancerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    @Column(length = 1000)
    private String bio;

    private String skills;

    private String experience;

    private String education;

    private String location;

    private String rate;

    // ADDED: Hours to Work
    private String hoursToWork;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    // ADDED: Getter and Setter for Hours to Work
    public String getHoursToWork() {
        return hoursToWork;
    }

    public void setHoursToWork(String hoursToWork) {
        this.hoursToWork = hoursToWork;
    }
}