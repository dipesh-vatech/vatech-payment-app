package com.vatech.payment.controller;

import com.vatech.payment.service.JwtUtil;
import com.vatech.payment.dto.AuthRequest;
import com.vatech.payment.service.AccountService;
import com.vatech.payment.entity.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    @Autowired
    private AccountService accountService;

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
}
