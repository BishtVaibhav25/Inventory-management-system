package com.ims.category;

import com.ims.common.ApiResponse;
import com.ims.common.DuplicateResourceException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryRepository categoryRepository;
	
	 // GET http://localhost:8080/api/categories
    // Returns all categories. Any authenticated user can access.
	@GetMapping
	public ResponseEntity<ApiResponse<List<Category>>> getAll(){
		List<Category> categories=categoryRepository.findAll();
		return ResponseEntity.ok(ApiResponse.success("Categories fetched successfully",categories));
	}
	

	 // POST http://localhost:8080/api/categories
    // Creates a new category. Only ADMIN and MANAGER (set in SecurityConfig).	
@PostMapping
public ResponseEntity<ApiResponse<Category>> create(@Valid @RequestBody Category category){
	
	// Check for duplicate name
	if (categoryRepository.existsByName(category.getName())) {
        throw new DuplicateResourceException(
                "Category with name '" + category.getName() + "' already exists");
    }
	Category saved=categoryRepository.save(category);
	return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Category created successfully", saved));
	
}


}