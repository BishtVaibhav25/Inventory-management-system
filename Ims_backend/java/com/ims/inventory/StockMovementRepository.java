package com.ims.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    // Get movement history for a product in a warehouse, newest first
    List<StockMovement> findByProductIdAndWarehouseIdOrderByCreatedAtDesc(
            Long productId, Long warehouseId);

    // Get all movements, newest first
    List<StockMovement> findAllByOrderByCreatedAtDesc();
}