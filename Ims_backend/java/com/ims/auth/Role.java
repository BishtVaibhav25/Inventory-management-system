package com.ims.auth;

// Why an enum and not a String?
// 1. Compile-time safety: you can't accidentally write "ADMN" or "admin"
// 2. Your frontend has exactly 3 roles — this enforces that in the database too
// 3. @Enumerated(EnumType.STRING) stores "ADMIN" in MySQL, not a number

public enum Role {
    ADMIN,
    MANAGER,
    STAFF
}