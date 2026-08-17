package com.ims.unit;

import com.ims.common.ApiResponse;
import com.ims.common.DuplicateResourceException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitRepository unitRepository;

    // GET http://localhost:8080/api/units
    @GetMapping
    public ResponseEntity<ApiResponse<List<Unit>>> getAll() {
        List<Unit> units = unitRepository.findAll();
        return ResponseEntity.ok(
                ApiResponse.success("Units fetched successfully", units));
    }

    // POST http://localhost:8080/api/units
    @PostMapping
    public ResponseEntity<ApiResponse<Unit>> create(
            @Valid @RequestBody Unit unit) {

        if (unitRepository.existsByName(unit.getName())) {
            throw new DuplicateResourceException(
                    "Unit with name '" + unit.getName() + "' already exists");
        }

        Unit saved = unitRepository.save(unit);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Unit created successfully", saved));
    }
}