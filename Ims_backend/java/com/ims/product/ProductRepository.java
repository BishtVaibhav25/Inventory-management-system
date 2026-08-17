package com.ims.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;



public interface ProductRepository extends JpaRepository<Product, Long> {

    // SELECT COUNT(*) > 0 FROM products WHERE sku = ?
    boolean existsBySku(String sku);

    // SELECT * FROM products WHERE category_id = ?
    // Used to find all products in a category
    List<Product> findByCategoryId(Long categoryId);

    // SELECT * FROM products WHERE active = true
    List<Product> findByActiveTrue();
    
    
   

 // SELECT * FROM products WHERE sku = ?
 Optional<Product> findBySku(String sku);
    
}