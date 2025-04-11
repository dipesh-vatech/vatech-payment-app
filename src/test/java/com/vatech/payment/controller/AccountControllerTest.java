package com.vatech.payment.controller;

import com.vatech.payment.dto.TransactionRequest;
import com.vatech.payment.entity.Account;
import com.vatech.payment.entity.Transaction;
import com.vatech.payment.entity.User;
import com.vatech.payment.service.AccountService;
import com.vatech.payment.service.EmailService;
import com.vatech.payment.service.JwtUtil;
import com.vatech.payment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        accountController = new AccountController();

        // Use reflection to inject mocks into private fields
        injectPrivateField(accountController, "accountService", accountService);
        injectPrivateField(accountController, "userRepository", userRepository);
        injectPrivateField(accountController, "emailService", emailService);
        injectPrivateField(accountController, "jwtUtil", jwtUtil);
    }

    private void injectPrivateField(Object target, String fieldName, Object fieldValue) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Make the private field accessible
        field.set(target, fieldValue); // Inject the mock object
    }

    @Test
    void testGetAccountDetails() {
        String token = "Bearer testToken";
        String username = "testUser";
        Account mockAccount = new Account();
        mockAccount.setAccountId(1L);

        when(jwtUtil.extractUsername("testToken")).thenReturn(username);
        when(accountService.getAccountDetails(username)).thenReturn(mockAccount);

        ResponseEntity<Account> response = accountController.getAccountDetails(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAccount, response.getBody());
    }

    @Test
    void testDeposit() {
        String token = "Bearer testToken";
        String username = "testUser";
        TransactionRequest request = new TransactionRequest();
        request.setAmount(200.0);

        when(jwtUtil.extractUsername("testToken")).thenReturn(username);
        doNothing().when(accountService).deposit(username, request.getAmount());

        ResponseEntity<String> response = accountController.deposit(token, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deposit successful", response.getBody());
    }

    @Test
    void testWithdraw() {
        String token = "Bearer testToken";
        String username = "testUser";
        TransactionRequest request = new TransactionRequest();
        request.setAmount(200.0);
        request.setAccountNumber("ACC1234567890");
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setUsername(username);

        when(jwtUtil.extractUsername("testToken")).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        doNothing().when(accountService).withdraw(username, request.getAmount());
        doNothing().when(emailService).sendWithdrawalNotification(
                mockUser.getEmail(),
                username,
                request.getAccountNumber(),
                request.getAmount()
        );

        ResponseEntity<String> response = accountController.withdraw(token, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Withdrawal successful", response.getBody());
    }

    @Test
    void testGetTransactionHistory() {
        String token = "Bearer testToken";
        String username = "testUser";
        Account mockAccount = new Account();
        mockAccount.setAccountId(1L);
        Transaction mockTransaction1 = new Transaction();
        Transaction mockTransaction2 = new Transaction();
        List<Transaction> mockTransactions = Arrays.asList(mockTransaction1, mockTransaction2);

        when(jwtUtil.extractUsername("testToken")).thenReturn(username);
        when(accountService.getAccountDetails(username)).thenReturn(mockAccount);
        when(accountService.getTransactionHistory(1L)).thenReturn(mockTransactions);

        ResponseEntity<List<Transaction>> response = accountController.getTransactionHistory(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTransactions, response.getBody());
    }
}