package com.vatech.payment.service;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    private final Key secretKey;

    // Ensure the secret key is properly loaded
    public JwtUtil(@Value("${jwt.secret.key}") String secret) {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("JWT secret key is missing. Please configure 'jwt.secret.key' in application.properties.");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//        System.out.println("Encoded Secret Key: " + new String(secretKey.getEncoded()));
    }

    // Generate JWT Token
    public String generateToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 min expiration
                .signWith(secretKey)
                .compact();

        // Log the generated token for debugging (optional)
//        System.out.println("Generated Token: " + token);
//        System.out.println("Secret Key Used: " + new String(secretKey.getEncoded()));

        return token; // Return the generated token
    }

    // Extract Username from Token
    public String extractUsername(String token) {
        try {
            token = token.replace("Bearer ", ""); // Remove Bearer prefix
//            System.out.println("Token Received for Validation: " + token);
//            System.out.println("Secret Key Used for Validation: " + new String(secretKey.getEncoded()));
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .setAllowedClockSkewSeconds(2)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
//            System.out.println("Token has expired: " + e.getMessage());
            throw new RuntimeException("Expired token", e);
        } catch (JwtException e) {
//            System.out.println("Invalid token: " + e.getMessage());
            throw new RuntimeException("Token validation failed", e);
        }
    }

    // Validate Token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
//        System.out.println("Extracted Username: " + username);
//        System.out.println("Expected Username: " + userDetails.getUsername());
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Check if Token is Expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract Expiration Date from Token
    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
