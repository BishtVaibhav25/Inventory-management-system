package com.ims.salesorder;

public enum SalesOrderStatus {
    PENDING,       // Order placed, awaiting confirmation
    CONFIRMED,     // Confirmed by manager
    SHIPPED,       // Shipped from warehouse → stock decreases
    DELIVERED,     // Customer received the goods
    CANCELLED      // Order was cancelled
}