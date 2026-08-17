package com.ims.ai;

import com.ims.inventory.StockLevelRepository;
import com.ims.product.Product;
import com.ims.product.ProductRepository;
import com.ims.purchaseorder.PurchaseOrderRepository;
import com.ims.salesorder.SalesOrderRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
public class InventoryAiTools {

    // ─── TOOL 1: Get stock level by SKU ───
    public record SkuRequest(String sku) {}
    public record StockResponse(
        String sku, String productName, String category,
        double price, int totalQuantity, String status
    ) {}

    @Bean
    @Description("Get the current stock level for a product by its SKU code. " +
                 "Returns product name, category, price, total quantity across all warehouses, " +
                 "and whether the product is IN_STOCK, LOW_STOCK, or OUT_OF_STOCK.")
    public Function<SkuRequest, StockResponse> getStockBySku(
            ProductRepository productRepo, StockLevelRepository stockRepo) {

        return request -> {
            Optional<Product> productOpt = productRepo.findBySku(
                request.sku() != null ? request.sku().trim().toUpperCase() : ""
            );

            if (productOpt.isEmpty()) {
                return new StockResponse(request.sku(), "NOT FOUND", "N/A", 0, 0, "PRODUCT_NOT_FOUND");
            }

            Product product = productOpt.get();
            int totalQty = stockRepo.findByProductId(product.getId())
                    .stream().mapToInt(s -> s.getQuantity()).sum();

            String status = totalQty == 0 ? "OUT_OF_STOCK" : totalQty <= 10 ? "LOW_STOCK" : "IN_STOCK";

            return new StockResponse(
                product.getSku(), product.getName(),
                product.getCategory() != null ? product.getCategory().getName() : "N/A",
                product.getPrice() != null ? product.getPrice().doubleValue() : 0.0,
                totalQty, status
            );
        };
    }

    // ─── TOOL 2: List all products ───
    public record EmptyRequest() {}
    public record ProductSummary(String sku, String name, String category, double price) {}
    public record ProductListResponse(int count, List<ProductSummary> products) {}

    @Bean
    @Description("List all products in the inventory system with their SKU, name, category, and price.")
    public Function<EmptyRequest, ProductListResponse> listAllProducts(ProductRepository productRepo) {
        return request -> {
            List<ProductSummary> products = productRepo.findAll().stream()
                .map(p -> new ProductSummary(
                    p.getSku(), p.getName(),
                    p.getCategory() != null ? p.getCategory().getName() : "N/A",
                    p.getPrice() != null ? p.getPrice().doubleValue() : 0.0
                )).collect(Collectors.toList());

            return new ProductListResponse(products.size(), products);
        };
    }

    // ─── TOOL 3: Get low stock alerts ───
    public record LowStockItem(String sku, String name, String warehouse, int quantity, int minStock) {}
    public record LowStockResponse(int alertCount, List<LowStockItem> items) {}

    @Bean
    @Description("Get all products that are currently below their minimum stock threshold.")
    public Function<EmptyRequest, LowStockResponse> getLowStockAlerts(StockLevelRepository stockRepo) {
        return request -> {
            List<LowStockItem> items = stockRepo.findAll().stream()
                .filter(s -> s.getMinStock() > 0 && s.getQuantity() <= s.getMinStock())
                .map(s -> new LowStockItem(
                    s.getProduct().getSku(), s.getProduct().getName(),
                    s.getWarehouse().getName(), s.getQuantity(), s.getMinStock()
                )).collect(Collectors.toList());

            return new LowStockResponse(items.size(), items);
        };
    }

    // ─── TOOL 4: Get order counts ───
    public record OrderCountResponse(long pendingPurchaseOrders, long pendingSalesOrders) {}

    @Bean
    @Description("Get the count of pending purchase orders and pending sales orders.")
    public Function<EmptyRequest, OrderCountResponse> getOrderCounts(
            PurchaseOrderRepository poRepo, SalesOrderRepository soRepo) {
        return request -> {
            long pendingPO = poRepo.countByStatus(com.ims.purchaseorder.PurchaseOrderStatus.PENDING);
            long pendingSO = soRepo.countByStatus(com.ims.salesorder.SalesOrderStatus.PENDING);
            return new OrderCountResponse(pendingPO, pendingSO);
        };
    }
}