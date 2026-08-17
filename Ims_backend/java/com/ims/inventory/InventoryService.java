package com.ims.inventory;

import com.ims.common.BadRequestException;
import com.ims.common.ResourceNotFoundException;
import com.ims.inventory.dto.StockAdjustRequest;
import com.ims.product.Product;
import com.ims.product.ProductService;
import com.ims.warehouse.Warehouse;
import com.ims.warehouse.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final StockLevelRepository stockLevelRepository;
    private final StockMovementRepository stockMovementRepository;
    private final ProductService productService;
    private final WarehouseService warehouseService;

    // ── GET ALL STOCK LEVELS ──
    public List<StockLevel> getAllStockLevels() {
        return stockLevelRepository.findAll();
    }

    // ── GET ALL MOVEMENTS ──
    public List<StockMovement> getAllMovements() {
        return stockMovementRepository.findAllByOrderByCreatedAtDesc();
    }

    // ── ADJUST STOCK ──
    // This is the CORE method. It:
    //   1. Validates product & warehouse exist
    //   2. Finds or creates the StockLevel row
    //   3. Updates the quantity
    //   4. Creates a StockMovement audit log entry
    //
    // @Transactional = if ANY step fails, ALL changes are ROLLED BACK.
    // Without it: stock_level could update but stock_movement could fail
    //             → data inconsistency! Transactional prevents this.
    @Transactional
    public StockMovement adjustStock(StockAdjustRequest request) {

        // 1. Validate product exists
        Product product = productService.getById(request.getProductId());

        // 2. Validate warehouse exists
        Warehouse warehouse = warehouseService.getById(request.getWarehouseId());

        // 3. Parse movement type
        StockMovementType type;
        try {
            type = StockMovementType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                    "Invalid type: " + request.getType() + ". Must be IN, OUT, or ADJUSTMENT");
        }

        // 4. Find existing StockLevel OR create a new one
        //    "Does a row exist for this product+warehouse combo?"
        StockLevel stockLevel = stockLevelRepository
                .findByProductIdAndWarehouseId(product.getId(), warehouse.getId())
                .orElseGet(() -> {
                    // First time this product is tracked in this warehouse
                    return StockLevel.builder()
                            .product(product)
                            .warehouse(warehouse)
                            .quantity(0)
                            .minStock(10)    // default thresholds
                            .maxStock(1000)
                            .build();
                });

        // 5. Calculate new quantity based on type
        int currentQty = stockLevel.getQuantity();
        int adjustQty = request.getQuantity();
        int newQty;
        int movementQty;    // what goes into the movement log

        switch (type) {
            case IN:
                newQty = currentQty + adjustQty;
                movementQty = adjustQty;       // positive
                break;
            case OUT:
                // Can't withdraw more than available!
                if (adjustQty > currentQty) {
                    throw new BadRequestException(
                            "Insufficient stock: only " + currentQty
                                    + " available, requested " + adjustQty);
                }
                newQty = currentQty - adjustQty;
                movementQty = -adjustQty;      // negative (stock going out)
                break;
            case ADJUSTMENT:
                // Adjustment delta (e.g., -5 for missing stock, +10 for found stock)
                newQty = currentQty + adjustQty;
                if (newQty < 0) {
                    throw new BadRequestException("Stock cannot be negative: current " + currentQty + ", adjustment " + adjustQty);
                }
                movementQty = adjustQty;
                break;
            default:
                throw new BadRequestException("Unknown type: " + type);
        }

        // 6. Update stock level
        stockLevel.setQuantity(newQty);
        stockLevelRepository.save(stockLevel);

        // 7. Create audit log (movement record)
        StockMovement movement = StockMovement.builder()
                .product(product)
                .warehouse(warehouse)
                .type(type)
                .quantity(movementQty)
                .reference(request.getReference())
                .notes(request.getNotes())
                .build();

        return stockMovementRepository.save(movement);
    }
}