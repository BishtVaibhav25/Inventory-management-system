package com.ims.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

// @Service = "Spring, manage this as a singleton bean. I can @Autowire it anywhere."

@Service
public class JwtService {

    // @Value reads from application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;          // 86400000 ms = 24 hours

    // === GENERATE TOKEN ===
    // Called after successful login. Creates a JWT string like:
    // eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiJ9.abc123...
    //
    // The token contains 3 parts (separated by dots):
    //   1. Header: algorithm info (HS256)
    //   2. Payload: username, role, name, expiration
    //   3. Signature: proof that YOUR server issued this (using the secret key)
    public String generateToken(String username, String role, String name) {
        return Jwts.builder()
                .subject(username)                                          // WHO this token is for
                .claims(Map.of("role", role, "name", name))                 // extra data we carry
                .issuedAt(new Date())                                       // WHEN created
                .expiration(new Date(System.currentTimeMillis() + expiration))  // WHEN it expires
                .signWith(getSigningKey())                                  // sign with secret
                .compact();                                                 // build the string
    }

    // === EXTRACT USERNAME from token ===
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // === EXTRACT ROLE from token ===
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // === VALIDATE TOKEN ===
    // Checks: (1) does the username match? (2) has it expired?
    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    // --- Private helpers ---

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())       // verify signature using our secret
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Converts the secret key string from application.properties into a crypto key
    private SecretKey getSigningKey() {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secretKey);
        } catch (Exception e) {
            keyBytes = secretKey.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}