package com.ims.purchaseorder.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreatePurchaseOrderRequest {

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    private LocalDate expectedDate;

    private Long productId;

    private Integer quantity;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be >= 0")
    private BigDecimal totalAmount;

    private String notes;
}