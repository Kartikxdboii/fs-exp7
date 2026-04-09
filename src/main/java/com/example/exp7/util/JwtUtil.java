package com.example.exp7.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JwtUtil - Helper class to CREATE and VALIDATE JWT tokens.
 *
 * A JWT token has 3 parts:
 *   1. Header   - algorithm used (HS256)
 *   2. Payload  - data like username, role, expiry
 *   3. Signature - ensures token hasn't been tampered with
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration; // in milliseconds (3600000 = 1 hour)

    // Create the signing key from our secret string
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate a JWT token for a user with their role.
     *
     * @param username - the user's username
     * @param role     - the user's role (ADMIN or USER)
     * @return signed JWT token string
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)                              // who this token is for
                .claim("role", role)                             // store role in token
                .issuedAt(new Date())                            // when token was created
                .expiration(new Date(System.currentTimeMillis() + expiration)) // when it expires
                .signWith(getSigningKey())                       // sign with our secret key
                .compact();
    }

    /**
     * Extract the username from a JWT token.
     */
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extract the role from a JWT token.
     */
    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    /**
     * Check if a token is valid (not expired and properly signed).
     */
    public boolean isTokenValid(String token) {
        try {
            getClaims(token); // this will throw an exception if invalid
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parse the token and get all claims (data stored inside).
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
