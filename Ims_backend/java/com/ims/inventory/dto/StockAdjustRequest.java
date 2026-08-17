package com.ims.inventory.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StockAdjustRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotBlank(message = "Type is required (IN, OUT, ADJUSTMENT)")
    private String type;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private String reference;    // "PO-001", "SO-003", etc.
    private String notes;
}