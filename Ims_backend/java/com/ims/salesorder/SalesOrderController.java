package com.ims.salesorder;

import com.ims.common.ApiResponse;
import com.ims.salesorder.dto.CreateSalesOrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.ims.salesorder.dto.UpdateSOStatusRequest;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SalesOrder>>> getAll() {
        List<SalesOrder> orders = salesOrderService.getAll();
        return ResponseEntity.ok(
                ApiResponse.success("Sales orders fetched successfully", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SalesOrder>> getById(@PathVariable Long id) {
        SalesOrder order = salesOrderService.getById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Sales order fetched successfully", order));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SalesOrder>> create(
            @Valid @RequestBody CreateSalesOrderRequest request) {
        SalesOrder order = salesOrderService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sales order created successfully", order));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SalesOrder>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSOStatusRequest request) {
        SalesOrder order = salesOrderService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(
                ApiResponse.success("Sales order status updated to " + request.getStatus(), order));
    }
}