package com.ims.warehouse;

import com.ims.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Warehouse>>> getAll() {
        List<Warehouse> warehouses = warehouseService.getAll();
        return ResponseEntity.ok(
                ApiResponse.success("Warehouses fetched successfully", warehouses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Warehouse>> getById(@PathVariable Long id) {
        Warehouse warehouse = warehouseService.getById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Warehouse fetched successfully", warehouse));
    }

    // No DTO needed here — Warehouse has no foreign keys.
    // The JSON body maps directly to the entity.
    // { "name": "Main Warehouse", "location": "Mumbai, MH", "capacity": 1000 }
    @PostMapping
    public ResponseEntity<ApiResponse<Warehouse>> create(
            @Valid @RequestBody Warehouse warehouse) {
        Warehouse saved = warehouseService.create(warehouse);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Warehouse created successfully", saved));
    }
}