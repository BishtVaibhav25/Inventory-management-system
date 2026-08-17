package com.ims.product;

import com.ims.common.ApiResponse;
import com.ims.product.dto.CreateProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAll() {
        List<Product> products = productService.getAll();
        return ResponseEntity.ok(
                ApiResponse.success("Products fetched successfully", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Product fetched successfully", product));
    }

    // NOW uses @Valid + DTO instead of raw Map
    // @Valid triggers all the @NotBlank, @NotNull, @DecimalMin annotations
    // on CreateProductRequest. If ANY fail → GlobalExceptionHandler returns 400.
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> create(
            @Valid @RequestBody CreateProductRequest request) {

        Product product = productService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", product));
    }
}