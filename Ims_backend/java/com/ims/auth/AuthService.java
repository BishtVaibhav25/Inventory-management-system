package com.ims.auth;

import com.ims.auth.dto.AuthResponse;
import com.ims.auth.dto.LoginRequest;
import com.ims.auth.dto.RegisterRequest;
import com.ims.common.BadRequestException;
import com.ims.common.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor     // Lombok: creates constructor with final fields (dependency injection)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;    // BCrypt — defined in SecurityConfig
    private final JwtService jwtService;

    // === REGISTER ===
    public AuthResponse register(RegisterRequest request) {

        // 1. Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException(
                    "Username '" + request.getUsername() + "' is already taken");
        }

        // 2. Validate role string → convert to enum
        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                    "Invalid role: " + request.getRole() + ". Must be ADMIN, MANAGER, or STAFF");
        }

        // 3. Build User with HASHED password
        // passwordEncoder.encode("demo") → "$2a$10$xyz..." (60-char BCrypt hash)
        // NEVER store plain text passwords!
        User user = User.builder()
                .username(request.getUsername())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        // 4. Save to database
        userRepository.save(user);

        // 5. Generate JWT and return
        String token = jwtService.generateToken(
                user.getUsername(), user.getRole().name(), user.getName());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    // === LOGIN ===
    public AuthResponse login(LoginRequest request) {

        // 1. Find user by username (throws custom exception if not found)
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid username or password"));

        // 2. Compare plain password with stored BCrypt hash
        // passwordEncoder.matches("demo", "$2a$10$xyz...") → true/false
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid username or password");
        }

        // 3. Generate JWT and return
        String token = jwtService.generateToken(
                user.getUsername(), user.getRole().name(), user.getName());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}