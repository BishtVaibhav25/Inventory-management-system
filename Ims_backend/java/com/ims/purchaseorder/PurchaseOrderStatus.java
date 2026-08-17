package com.ims.purchaseorder;

public enum PurchaseOrderStatus {
    PENDING,       // Just created, waiting for approval
    APPROVED,      // Manager approved the order
    SHIPPED,       // Supplier shipped the goods
    RECEIVED,      // Goods arrived at warehouse → stock increases
    CANCELLED      // Order was cancelled
}