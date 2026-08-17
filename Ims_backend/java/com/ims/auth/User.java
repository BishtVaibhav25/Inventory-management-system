package com.ims.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity                        // "This class maps to a database table"
@Table(name = "users")         // Table name = "users" (not "user" which is a MySQL reserved word)
@Data                          // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor             // Lombok: generates User() — required by JPA
@AllArgsConstructor            // Lombok: generates User(id, username, name, password, role)
@Builder                       // Lombok: enables User.builder().username("admin").build()
public class User {

    @Id                                                    // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY)    // AUTO_INCREMENT
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
    @Column(unique = true, nullable = false)               // UNIQUE constraint in DB
    private String username;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters")
    @Column(nullable = false)
    private String password;                               // Stored as BCrypt hash, never plain text

    @Enumerated(EnumType.STRING)   // Store "ADMIN" as string, not 0
    @Column(nullable = false)
    private Role role;
}