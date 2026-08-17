package com.ims.auth;

import com.ims.auth.dto.UpdateUserRoleRequest;
import com.ims.auth.dto.UserResponse;
import com.ims.common.ApiResponse;
import com.ims.common.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userRepository.findAll().stream()
                .map(u -> UserResponse.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .name(u.getName())
                        .role(u.getRole())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", users));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRoleRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setRole(request.getRole());
        User updated = userRepository.save(user);

        UserResponse response = UserResponse.builder()
                .id(updated.getId())
                .username(updated.getUsername())
                .name(updated.getName())
                .role(updated.getRole())
                .build();

        return ResponseEntity.ok(ApiResponse.success("User role updated successfully to " + updated.getRole(), response));
    }
}
