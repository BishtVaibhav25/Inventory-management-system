package com.ims.supplier;

import com.ims.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Supplier>>> getAll() {
        List<Supplier> suppliers = supplierService.getAll();
        return ResponseEntity.ok(
                ApiResponse.success("Suppliers fetched successfully", suppliers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Supplier>> getById(@PathVariable Long id) {
        Supplier supplier = supplierService.getById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Supplier fetched successfully", supplier));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Supplier>> create(
            @Valid @RequestBody Supplier supplier) {
        Supplier saved = supplierService.create(supplier);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Supplier created successfully", saved));
    }
}