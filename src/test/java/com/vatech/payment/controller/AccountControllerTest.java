//package com.vatech.payment.controller;
//
//import com.vatech.payment.entity.Account;
//import com.vatech.payment.service.AccountService;
//import com.vatech.payment.service.JwtUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//public class AccountControllerTest {
//
//    @InjectMocks
//    private AccountController accountController;
//
//    @Mock
//    private AccountService accountService;
//
//    @Mock
//    private JwtUtil jwtUtil;
//
//    private String token;
//    private String username;
//    private Account account;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Test data initialization
//        token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEaXBlc2giLCJpYXQiOjE3NDM1Mjk0ODksImV4cCI6MTc0MzYxNTg4OX0.wsb9KbIivtymRroE6rdKKQyRXoseCGfvOt_abXfk15U"; // Full token with "Bearer " prefix
//        username = "testuser";
//
//        account = new Account();
//        account.setUsername(username);
//        // Set additional fields as necessary
//    }
//
//    @Test
//    public void testGetAccountDetails() {
//        // Arrange: Mock behavior of JwtUtil and AccountService
//        String strippedToken = token.replace("Bearer ", ""); // Match stripping logic in JwtUtil
//        when(jwtUtil.extractUsername(strippedToken)).thenReturn(username); // Mock username extraction
//        when(accountService.getAccountDetails(username)).thenReturn(account); // Mock account retrieval
//
//        // Act: Call the controller method
//        Account result = accountController.getAccountDetails(token);
//
//        // Assert: Ensure the returned account matches the mock
//        assertEquals(account, result);
//    }
//}
