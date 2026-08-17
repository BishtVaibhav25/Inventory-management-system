package com.ims.salesorder;

import com.ims.common.ResourceNotFoundException;
import com.ims.customer.Customer;
import com.ims.customer.CustomerService;
import com.ims.salesorder.dto.CreateSalesOrderRequest;
import com.ims.warehouse.Warehouse;
import com.ims.warehouse.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import com.ims.inventory.InventoryService;
import com.ims.inventory.dto.StockAdjustRequest;
import com.ims.product.Product;
import com.ims.product.ProductService;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerService customerService;
    private final WarehouseService warehouseService;
    private final ProductService productService;
    private final InventoryService inventoryService;

    public List<SalesOrder> getAll() {
        return salesOrderRepository.findAll();
    }

    public SalesOrder getById(Long id) {
        return salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sales order not found with id: " + id));
    }

    @Transactional
    public SalesOrder create(CreateSalesOrderRequest request) {

        Customer customer = customerService.getById(request.getCustomerId());
        Warehouse warehouse = warehouseService.getById(request.getWarehouseId());
        Product product = request.getProductId() != null ? productService.getById(request.getProductId()) : null;

        long count = salesOrderRepository.count();
        String orderNumber = String.format("SO-%03d", count + 1);

        SalesOrder so = SalesOrder.builder()
                .orderNumber(orderNumber)
                .customer(customer)
                .warehouse(warehouse)
                .product(product)
                .quantity(request.getQuantity())
                .orderDate(request.getOrderDate())
                .deliveryDate(request.getDeliveryDate())
                .status(SalesOrderStatus.PENDING)
                .totalAmount(request.getTotalAmount())
                .notes(request.getNotes())
                .build();

        return salesOrderRepository.save(so);
    }

    @Transactional
    public SalesOrder updateStatus(Long id, SalesOrderStatus newStatus) {
        SalesOrder so = getById(id);
        SalesOrderStatus oldStatus = so.getStatus();

        so.setStatus(newStatus);
        SalesOrder savedSo = salesOrderRepository.save(so);

        // ── AUTOMATED INVENTORY TRIGGER: Step 2 in Sales Order Lifecycle ──
        // When SO becomes SHIPPED, trigger OUT stock adjustment (deduct stock)
        if (newStatus == SalesOrderStatus.SHIPPED && oldStatus != SalesOrderStatus.SHIPPED) {
            if (savedSo.getProduct() != null && savedSo.getQuantity() != null && savedSo.getQuantity() > 0) {
                StockAdjustRequest adjustReq = new StockAdjustRequest();
                adjustReq.setProductId(savedSo.getProduct().getId());
                adjustReq.setWarehouseId(savedSo.getWarehouse().getId());
                adjustReq.setType("OUT");
                adjustReq.setQuantity(savedSo.getQuantity());
                adjustReq.setReference(savedSo.getOrderNumber());
                adjustReq.setNotes("SO Shipped: " + savedSo.getOrderNumber());

                inventoryService.adjustStock(adjustReq);
            }
        }

        return savedSo;
    }
}