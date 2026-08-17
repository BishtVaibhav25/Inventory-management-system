package com.ims.product;

import com.ims.category.Category;
import com.ims.category.CategoryRepository;
import com.ims.common.DuplicateResourceException;
import com.ims.common.ResourceNotFoundException;
import com.ims.product.dto.CreateProductRequest;
import com.ims.unit.Unit;
import com.ims.unit.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UnitRepository unitRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + id));
    }

    // Now accepts a validated DTO — much cleaner!
    public Product create(CreateProductRequest request) {

        // 1. Check duplicate SKU
        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException(
                    "Product with SKU '" + request.getSku() + "' already exists");
        }

        // 2. Fetch Category (validates it exists)
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + request.getCategoryId()));

        // 3. Fetch Unit (validates it exists)
        Unit unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Unit not found with id: " + request.getUnitId()));

        // 4. Convert DTO → Entity
        Product product = Product.builder()
                .sku(request.getSku())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(category)
                .unit(unit)
                .active(true)
                .build();

        return productRepository.save(product);
    }
}