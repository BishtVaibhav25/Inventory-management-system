package com.ims.purchaseorder;

import com.ims.common.ResourceNotFoundException;
import com.ims.purchaseorder.dto.CreatePurchaseOrderRequest;
import com.ims.supplier.Supplier;
import com.ims.supplier.SupplierService;
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
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierService supplierService;
    private final WarehouseService warehouseService;
    private final ProductService productService;
    private final InventoryService inventoryService;

    public List<PurchaseOrder> getAll() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder getById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Purchase order not found with id: " + id));
    }

    @Transactional
    public PurchaseOrder create(CreatePurchaseOrderRequest request) {

        Supplier supplier = supplierService.getById(request.getSupplierId());
        Warehouse warehouse = warehouseService.getById(request.getWarehouseId());
        Product product = request.getProductId() != null ? productService.getById(request.getProductId()) : null;

        long count = purchaseOrderRepository.count();
        String orderNumber = String.format("PO-%03d", count + 1);

        PurchaseOrder po = PurchaseOrder.builder()
                .orderNumber(orderNumber)
                .supplier(supplier)
                .warehouse(warehouse)
                .product(product)
                .quantity(request.getQuantity())
                .orderDate(request.getOrderDate())
                .expectedDate(request.getExpectedDate())
                .status(PurchaseOrderStatus.PENDING)
                .totalAmount(request.getTotalAmount())
                .notes(request.getNotes())
                .build();

        return purchaseOrderRepository.save(po);
    }

    @Transactional
    public PurchaseOrder updateStatus(Long id, PurchaseOrderStatus newStatus) {
        PurchaseOrder po = getById(id);
        PurchaseOrderStatus oldStatus = po.getStatus();

        po.setStatus(newStatus);
        PurchaseOrder savedPo = purchaseOrderRepository.save(po);

        // ── AUTOMATED INVENTORY TRIGGER: Step 2 in Purchase Order Lifecycle ──
        // When PO becomes RECEIVED, trigger IN stock adjustment
        if (newStatus == PurchaseOrderStatus.RECEIVED && oldStatus != PurchaseOrderStatus.RECEIVED) {
            if (savedPo.getProduct() != null && savedPo.getQuantity() != null && savedPo.getQuantity() > 0) {
                StockAdjustRequest adjustReq = new StockAdjustRequest();
                adjustReq.setProductId(savedPo.getProduct().getId());
                adjustReq.setWarehouseId(savedPo.getWarehouse().getId());
                adjustReq.setType("IN");
                adjustReq.setQuantity(savedPo.getQuantity());
                adjustReq.setReference(savedPo.getOrderNumber());
                adjustReq.setNotes("PO Received: " + savedPo.getOrderNumber());

                inventoryService.adjustStock(adjustReq);
            }
        }

        return savedPo;
    }
}