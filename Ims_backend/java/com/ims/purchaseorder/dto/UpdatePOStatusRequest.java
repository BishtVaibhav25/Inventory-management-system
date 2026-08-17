package com.ims.purchaseorder.dto;

import com.ims.purchaseorder.PurchaseOrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePOStatusRequest {

    @NotNull(message = "Status is required")
    private PurchaseOrderStatus status;
}
