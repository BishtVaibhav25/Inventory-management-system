package com.ims.purchaseorder;

import com.ims.common.ApiResponse;
import com.ims.purchaseorder.dto.CreatePurchaseOrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.ims.purchaseorder.dto.UpdatePOStatusRequest;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PurchaseOrder>>> getAll() {
        List<PurchaseOrder> orders = purchaseOrderService.getAll();
        return ResponseEntity.ok(
                ApiResponse.success("Purchase orders fetched successfully", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseOrder>> getById(@PathVariable Long id) {
        PurchaseOrder order = purchaseOrderService.getById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Purchase order fetched successfully", order));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseOrder>> create(
            @Valid @RequestBody CreatePurchaseOrderRequest request) {
        PurchaseOrder order = purchaseOrderService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Purchase order created successfully", order));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PurchaseOrder>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePOStatusRequest request) {
        PurchaseOrder order = purchaseOrderService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(
                ApiResponse.success("Purchase order status updated to " + request.getStatus(), order));
    }
}