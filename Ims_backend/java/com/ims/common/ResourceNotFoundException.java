package com.ims.common;

// extends RuntimeException = this is an UNCHECKED exception.
// Unchecked means you don't have to write throws/try-catch everywhere.
// Spring will propagate it up to the GlobalExceptionHandler automatically.

public class ResourceNotFoundException extends RuntimeException {

    // The message you pass here (e.g., "Product not found with id: 99")
    // becomes ex.getMessage() in the exception handler.
    public ResourceNotFoundException(String message) {
        super(message);
    }
}