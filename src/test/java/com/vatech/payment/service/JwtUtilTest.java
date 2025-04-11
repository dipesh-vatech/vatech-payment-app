package com.vatech.payment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private Key secretKey;

    @BeforeEach
    public void setUp() {
        // Generate a secure key for HMAC-SHA256
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        jwtUtil = new JwtUtil(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
    }

    @Test
    public void testGenerateToken() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ")); // JWT tokens start with "eyJ"
    }

    @Test
    public void testExtractUsername() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    public void testValidateToken() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    public void testValidateTokenWithInvalidUser() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("anotherUser");

        assertFalse(jwtUtil.validateToken(token, userDetails));
    }

//    @Test
//    public void testExtractUsernameWithExpiredToken() {
//        String username = "testUser";
//
//        // Generate a token with a short expiration time (e.g., 1 second)
//        String token = jwtUtil.generateToken(username);
//
//        // Simulate token expiration by waiting for 2 seconds
//        try {
//            Thread.sleep(2000); // Sleep for 2 seconds to ensure the token is expired
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            jwtUtil.extractUsername(token);
//        });
//
//        assertTrue(exception.getMessage().contains("Expired token"));
//    }

    @Test
    public void testExtractUsernameWithInvalidToken() {
        String invalidToken = "invalid.token.string";

        Exception exception = assertThrows(RuntimeException.class, () -> {
            jwtUtil.extractUsername(invalidToken);
        });

        assertTrue(exception.getMessage().contains("Token validation failed"));
    }
}
