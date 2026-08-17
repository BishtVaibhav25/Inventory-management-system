package com.ims.unit;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "units")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Unit name is required")
    @Size(max = 50, message = "Unit name must not exceed 50 characters")
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @NotBlank(message = "Abbreviation is required")
    @Size(max = 10, message = "Abbreviation must not exceed 10 characters")
    @Column(nullable = false, length = 10)
    private String abbreviation;
}