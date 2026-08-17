package com.ims.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockLevelRepository extends JpaRepository<StockLevel, Long> {

    // Find stock for a specific product in a specific warehouse
    Optional<StockLevel> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    // Find all stock levels for a product (across all warehouses)
    List<StockLevel> findByProductId(Long productId);

    // Dashboard: count items where quantity <= minStock (low stock alerts)
    @Query("SELECT COUNT(s) FROM StockLevel s WHERE s.quantity <= s.minStock AND s.minStock > 0")
    long countLowStock();

    // Dashboard: total stock value = SUM(quantity * product.price)
    @Query("SELECT COALESCE(SUM(s.quantity * s.product.price), 0) FROM StockLevel s")
    java.math.BigDecimal calculateTotalStockValue();
}