package com.ims.auth.dto;

import lombok.*;

// What the backend sends BACK after successful login/register.
// The frontend stores this (especially the token) in localStorage.
// Every future API call will include: Authorization: Bearer <token>

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;       // JWT token string (the "key" to access protected APIs)
    private String username;    // "admin"
    private String name;        // "Vaibhav Bisht"
    private String role;        // "ADMIN"
}