package com.vatech.payment.controller;

import com.vatech.payment.dto.AuthRequest;
import com.vatech.payment.service.JwtUtil;
import com.vatech.payment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController();

        // Use reflection to inject mocks into private fields
        injectPrivateField(authController, "authenticationManager", authenticationManager);
        injectPrivateField(authController, "jwtUtil", jwtUtil);
        injectPrivateField(authController, "userService", userService);
    }

    private void injectPrivateField(Object target, String fieldName, Object fieldValue) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Make the private field accessible
        field.set(target, fieldValue); // Inject the mock object
    }

    @Test
    void testLogin() {
        // Create an AuthRequest object with test data
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPassword");

        // Mock the authentication process
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Mock JWT generation
        when(jwtUtil.generateToken("testUser")).thenReturn("testToken");

        // Perform the login request
        ResponseEntity<Map<String, String>> response = authController.login(authRequest);

        // Validate the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testToken", response.getBody().get("token"));

        // Verify mocks
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken("testUser");
    }

    @Test
    void testRegister() {
        // Create an AuthRequest object with test data
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPassword");
        authRequest.setEmail("test@example.com");

        // Mock the registration process
        when(userService.registerUser("testUser", "testPassword", "test@example.com"))
                .thenReturn("ACC123456789");

        // Perform the registration request
        ResponseEntity<Map<String, String>> response = authController.register(authRequest);

        // Validate the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody().get("message"));
        assertEquals("ACC123456789", response.getBody().get("accountNumber"));
        assertEquals("test@example.com", response.getBody().get("email"));

        // Verify mocks
        verify(userService).registerUser("testUser", "testPassword", "test@example.com");
    }
}