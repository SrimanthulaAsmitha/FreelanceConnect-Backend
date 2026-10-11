package com.FreelanceConnect.FreelanceConnect.controller;

import com.FreelanceConnect.FreelanceConnect.entity.FreelancerProfile;
import com.FreelanceConnect.FreelanceConnect.repository.FreelancerProfileRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final FreelancerProfileRepository profileRepository;

    public ProfileController(
            FreelancerProfileRepository profileRepository) {

        this.profileRepository = profileRepository;
    }

    // GET MY PROFILE
    @GetMapping
    public FreelancerProfile getMyProfile(
            Authentication authentication) {

        String email = authentication.getName();

        return profileRepository
                .findByEmail(email)
                .orElseGet(() -> {

                    FreelancerProfile profile =
                            new FreelancerProfile();

                    profile.setEmail(email);

                    return profileRepository.save(profile);
                });
    }

    // CREATE / UPDATE MY PROFILE
    @PutMapping
    public FreelancerProfile updateMyProfile(
            @RequestBody FreelancerProfile profile,
            Authentication authentication) {

        String email = authentication.getName();

        FreelancerProfile existingProfile =
                profileRepository
                        .findByEmail(email)
                        .orElse(new FreelancerProfile());

        existingProfile.setEmail(email);
        existingProfile.setName(profile.getName());
        existingProfile.setBio(profile.getBio());
        existingProfile.setSkills(profile.getSkills());
        existingProfile.setExperience(profile.getExperience());
        existingProfile.setEducation(profile.getEducation());
        existingProfile.setLocation(profile.getLocation());
        existingProfile.setRate(profile.getRate());

        // ADDED: Save Hours to Work
        existingProfile.setHoursToWork(profile.getHoursToWork());

        return profileRepository.save(existingProfile);
    }

    // GET FREELANCER PROFILE BY EMAIL
    @GetMapping("/freelancer/{email}")
    public FreelancerProfile getFreelancerProfile(
            @PathVariable String email) {

        System.out.println(
                "PROFILE REQUEST RECEIVED: " + email
        );

        return profileRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Freelancer profile not found"
                        )
                );
    }
}