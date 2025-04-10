package com.vatech.payment.controller;

import com.vatech.payment.service.JwtUtil;
import com.vatech.payment.dto.AuthRequest;
import com.vatech.payment.service.AccountService;
import com.vatech.payment.entity.Account;
import com.vatech.payment.entity.User;
import com.vatech.payment.repository.UserRepository;
import com.vatech.payment.service.EmailService;
import com.vatech.payment.dto.TransactionRequest;
import com.vatech.payment.entity.Transaction;
import com.vatech.payment.dto.TransactionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/account")
    public ResponseEntity<Account> getAccountDetails(@RequestHeader("Authorization") String token) {
        try {
            // Log received token
//            System.out.println("Received Token: " + token);

            // Extract username from JWT token
            String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
//            System.out.println("Extracted Username: " + username);

            // Fetch account details
            Account account = accountService.getAccountDetails(username);
            if (account == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // No account found
            }

            // Return account details
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            // Log and return error
//            System.out.println("Error fetching account details: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestHeader("Authorization") String token, @RequestBody TransactionRequest request) {
        try {
            // Extract username from JWT token
            String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));

            // Process deposit
            accountService.deposit(username, request.getAmount());
            return ResponseEntity.ok("Deposit successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to process deposit: " + e.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestHeader("Authorization") String token, @RequestBody TransactionRequest request) {
        try {
            // Extract username from JWT token
            String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));

            // Fetch the user based on the username
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Process the withdrawal
            accountService.withdraw(username, request.getAmount());

            // Send email notification to the user's email address
            emailService.sendWithdrawalNotification(
                    user.getEmail(),       // Recipient's email
                    user.getUsername(),    // Username
                    request.getAccountNumber(), // Account number
                    request.getAmount()    // Withdrawal amount
            );

            return ResponseEntity.ok("Withdrawal successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to process withdrawal: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@RequestHeader("Authorization") String token) {
        try {
            // Extract username from JWT token
            String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));

            // Fetch account details to get accountId
            Account account = accountService.getAccountDetails(username);
            Long accountId = account.getAccountId(); // Extract accountId

            // Fetch transaction history
            List<Transaction> transactions = accountService.getTransactionHistory(accountId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
