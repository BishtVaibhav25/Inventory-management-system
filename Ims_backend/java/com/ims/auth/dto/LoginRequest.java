package com.ims.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// This is what the frontend sends when user clicks "Sign in":
// POST /api/auth/login
// Body: { "username": "admin", "password": "demo" }

@Data
public class LoginRequest {

    @NotBlank(message = "Username is required")   // Rejects null, "", and "   "
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}