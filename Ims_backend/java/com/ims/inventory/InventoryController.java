package com.ims.inventory;

import com.ims.common.ApiResponse;
import com.ims.inventory.dto.StockAdjustRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // GET /api/inventory — all stock levels
    @GetMapping
    public ResponseEntity<ApiResponse<List<StockLevel>>> getAllStockLevels() {
        List<StockLevel> levels = inventoryService.getAllStockLevels();
        return ResponseEntity.ok(
                ApiResponse.success("Stock levels fetched successfully", levels));
    }

    // GET /api/inventory/movements — all stock movements (audit log)
    @GetMapping("/movements")
    public ResponseEntity<ApiResponse<List<StockMovement>>> getAllMovements() {
        List<StockMovement> movements = inventoryService.getAllMovements();
        return ResponseEntity.ok(
                ApiResponse.success("Stock movements fetched successfully", movements));
    }

    // POST /api/inventory/adjust — adjust stock (IN/OUT/ADJUSTMENT)
    @PostMapping("/adjust")
    public ResponseEntity<ApiResponse<StockMovement>> adjustStock(
            @Valid @RequestBody StockAdjustRequest request) {
        StockMovement movement = inventoryService.adjustStock(request);
        return ResponseEntity.ok(
                ApiResponse.success("Stock adjusted successfully", movement));
    }
}