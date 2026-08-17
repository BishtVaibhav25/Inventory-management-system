package com.ims.category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {

	
	// Spring auto-generates: SELECT COUNT(*) > 0 FROM categories WHERE name = ?
	boolean existsByName(String name);
}
