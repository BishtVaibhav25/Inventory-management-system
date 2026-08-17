package com.ims.customer;

import com.ims.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Customer>>> getAll() {
        List<Customer> customers = customerService.getAll();
        return ResponseEntity.ok(
                ApiResponse.success("Customers fetched successfully", customers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> getById(@PathVariable Long id) {
        Customer customer = customerService.getById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Customer fetched successfully", customer));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Customer>> create(
            @Valid @RequestBody Customer customer) {
        Customer saved = customerService.create(customer);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer created successfully", saved));
    }
}