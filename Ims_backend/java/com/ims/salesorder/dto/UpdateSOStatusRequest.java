package com.ims.salesorder.dto;

import com.ims.salesorder.SalesOrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateSOStatusRequest {

    @NotNull(message = "Status is required")
    private SalesOrderStatus status;
}
