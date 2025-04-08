package com.vatech.payment.service;

import com.vatech.payment.entity.User;
import com.vatech.payment.entity.Account;
import com.vatech.payment.repository.UserRepository;
import com.vatech.payment.repository.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,AccountRepository accountRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username already exists");
        }
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        User savedUser = userRepository.save(user);

        // Create and save the account
        Account account = new Account();
        account.setUser(savedUser); // Link the user to the account
        account.setAccountNumber(generateAccountNumber()); // Assign a unique account number
        account.setAccountBalance(0.0); // Initialize balance to 0
        accountRepository.save(account);

        return "User registered and account created successfully";
    }

    // Method to generate a unique account number
    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis(); // Example: Use current timestamp for uniqueness
    }
}