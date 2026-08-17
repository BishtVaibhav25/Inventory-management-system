package com.ims.auth;

import com.ims.auth.dto.AuthResponse;
import com.ims.auth.dto.LoginRequest;
import com.ims.auth.dto.RegisterRequest;
import com.ims.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RestController = @Controller + @ResponseBody
//   Every method return value is automatically converted to JSON
// @RequestMapping("/api/auth") = base URL prefix for all methods in this class

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST http://localhost:8080/api/auth/register
    // @Valid = trigger validation annotations on RegisterRequest before entering this method
    //          If validation fails → MethodArgumentNotValidException → GlobalExceptionHandler
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse data = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)              // 201 Created
                .body(ApiResponse.success("User registered successfully", data));
    }

    // POST http://localhost:8080/api/auth/login
    // This is what your Login.jsx will call
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse data = authService.login(request);
        return ResponseEntity
                .ok(ApiResponse.success("Login successful", data));
    }
}