
package com.ims.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;

// @JsonInclude(NON_NULL) = if "data" is null, don't include it in the JSON output.
// This keeps error responses clean — they won't have "data": null cluttering them.

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;       // true = all good, false = something went wrong
    private String message;        // human-readable message
    private T data;                // the actual payload (generic — can be anything)

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // --- Factory methods (shortcuts to create responses) ---

    // Success WITH data: ApiResponse.success("Found 7 products", productList)
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    // Success WITHOUT data: ApiResponse.success("Stock adjusted")
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .build();
    }

    // Error: ApiResponse.error("Product not found")
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}