package com.ims.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ims.product.Product;
import com.ims.warehouse.Warehouse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "stock_levels",
       // UNIQUE constraint: only ONE row per product-warehouse combination
       // You can't have two rows saying "Laptop in Main Warehouse"
       uniqueConstraints = @UniqueConstraint(
               columnNames = {"product_id", "warehouse_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK: Which product?
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Product product;

    // FK: In which warehouse?
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Warehouse warehouse;

    @Builder.Default
    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(nullable = false)
    private int quantity = 0;

    // Alert threshold — if quantity drops below this, show warning on dashboard
    @Builder.Default
    @Column(name = "min_stock", nullable = false)
    private int minStock = 0;

    @Builder.Default
    @Column(name = "max_stock", nullable = false)
    private int maxStock = 0;
}