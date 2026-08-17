package com.ims.inventory;

// IN         = Stock coming in (purchase order received, returned goods)
// OUT        = Stock going out (sales order shipped, damaged goods)
// ADJUSTMENT = Manual correction (physical count mismatch)

public enum StockMovementType {
    IN,
    OUT,
    ADJUSTMENT
}