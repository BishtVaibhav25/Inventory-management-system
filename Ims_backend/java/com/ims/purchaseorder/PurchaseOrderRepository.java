package com.ims.purchaseorder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    // For dashboard: count pending orders
    long countByStatus(PurchaseOrderStatus status);

    // Find all orders from a specific supplier
    List<PurchaseOrder> findBySupplierIdOrderByOrderDateDesc(Long supplierId);
}