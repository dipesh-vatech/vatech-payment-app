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

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Updated method to include email
    public String registerUser(String username, String rawPassword, String email) {
        // Check if the username already exists
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username already exists");
        }

        // Check if the email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        // Encrypt the password
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // Create a new User object and set its properties
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEmail(email); // Set the email field

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Create and save the associated account
        Account account = new Account();
        account.setUser(savedUser); // Link the account to the user
        account.setAccountNumber(generateAccountNumber()); // Generate a unique account number
        account.setAccountBalance(0.0); // Initialize balance to 0
        accountRepository.save(account);

        // Return the generated account number
        return account.getAccountNumber();
    }

    // Method to generate a unique account number
    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis(); // Example: Use the current timestamp for uniqueness
    }
}