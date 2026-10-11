package com.FreelanceConnect.FreelanceConnect.controller;

import com.FreelanceConnect.FreelanceConnect.dto.LoginRequest;
import com.FreelanceConnect.FreelanceConnect.dto.LoginResponse;
import com.FreelanceConnect.FreelanceConnect.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        return authService.login(request);
    }
}