package com.ims.dashboard.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {

    private long totalProducts;
    private long totalWarehouses;
    private long totalSuppliers;
    private long totalCustomers;
    private BigDecimal totalStockValue;
    private long lowStockAlerts;
    private long pendingPurchaseOrders;
    private long pendingSalesOrders;
}