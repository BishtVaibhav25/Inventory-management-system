package com.ims.dashboard;

import com.ims.customer.CustomerRepository;
import com.ims.dashboard.dto.DashboardStatsResponse;
import com.ims.inventory.StockLevelRepository;
import com.ims.product.ProductRepository;
import com.ims.purchaseorder.PurchaseOrderRepository;
import com.ims.purchaseorder.PurchaseOrderStatus;
import com.ims.salesorder.SalesOrderRepository;
import com.ims.salesorder.SalesOrderStatus;
import com.ims.supplier.SupplierRepository;
import com.ims.warehouse.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;
    private final CustomerRepository customerRepository;
    private final StockLevelRepository stockLevelRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SalesOrderRepository salesOrderRepository;

    public DashboardStatsResponse getStats() {
        return DashboardStatsResponse.builder()
                .totalProducts(productRepository.count())
                .totalWarehouses(warehouseRepository.count())
                .totalSuppliers(supplierRepository.count())
                .totalCustomers(customerRepository.count())
                .totalStockValue(stockLevelRepository.calculateTotalStockValue())
                .lowStockAlerts(stockLevelRepository.countLowStock())
                .pendingPurchaseOrders(
                        purchaseOrderRepository.countByStatus(PurchaseOrderStatus.PENDING))
                .pendingSalesOrders(
                        salesOrderRepository.countByStatus(SalesOrderStatus.PENDING))
                .build();
    }
}