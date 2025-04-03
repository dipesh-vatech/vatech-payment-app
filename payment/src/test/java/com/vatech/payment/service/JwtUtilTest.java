//package com.vatech.payment.service;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.lang.reflect.Field;
//import java.security.Key;
//import java.util.Date;
//import static org.junit.jupiter.api.Assertions.*;
//
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.security.Keys;
//public class JwtUtilTest {
//
//    private JwtUtil jwtUtil;
//    private Key secretKey;
//
//    @BeforeEach
//    public void setUp() throws NoSuchFieldException, IllegalAccessException {
//        jwtUtil = new JwtUtil();
//
//        // Use reflection to access the private `secretKey` field in JwtUtil
//        Field secretKeyField = JwtUtil.class.getDeclaredField("secretKey");
//        secretKeyField.setAccessible(true);
//        secretKey = (Key) secretKeyField.get(jwtUtil);
//    }
//
//    @Test
//    public void testGenerateToken() {
//        // Arrange
//        String username = "testuser";
//
//        // Act
//        String token = jwtUtil.generateToken(username);
//
//        // Assert
//        assertNotNull(token);
//        assertTrue(token.startsWith("eyJ")); // Ensure it's a valid JWT format
//    }
//
//    @Test
//    public void testExtractUsername_ValidToken() {
//        // Arrange
//        String username = "testuser";
//        String token = Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day validity
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//
//        // Act
//        String extractedUsername = jwtUtil.extractUsername("Bearer " + token);
//
//        // Assert
//        assertEquals(username, extractedUsername);
//    }
//
//    @Test
//    public void testExtractUsername_ExpiredToken() {
//        // Arrange
//        String username = "testuser";
//        String expiredToken = Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis() - 86400000)) // Issued 1 day ago
//                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expired
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            jwtUtil.extractUsername("Bearer " + expiredToken);
//        });
//        assertEquals("Expired token", exception.getMessage());
//    }
//
//    @Test
//    public void testExtractUsername_InvalidToken() {
//        // Arrange
//        String invalidToken = "Bearer invalid.token";
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            jwtUtil.extractUsername(invalidToken);
//        });
//        assertEquals("Token validation failed", exception.getMessage());
//    }
//
//    @Test
//    public void testExtractUsername_EmptyToken() {
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            jwtUtil.extractUsername("");
//        });
//        assertEquals("Token validation failed", exception.getMessage());
//    }
//
//    @Test
//    public void testExtractUsername_NullToken() {
//        // Act & Assert
//        RuntimeException exception = assertThrows(NullPointerException.class, () -> {
//            jwtUtil.extractUsername(null);
//        });
//        assertEquals("Token validation failed", exception.getMessage());
//    }
//}
