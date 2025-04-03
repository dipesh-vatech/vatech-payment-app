package com.vatech.payment.service;

import com.vatech.payment.entity.User;
import com.vatech.payment.repository.UserRepository;

import com.vatech.payment.repository.AccountRepository;
import com.vatech.payment.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public Account getAccountDetails(String username) {
        // Fetch user by username, handle Optional
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found for username: " + username));

        // Fetch account details using user_id
        Account account = accountRepository.findAccountByUsername(username);
        if (account == null) {
            throw new RuntimeException("Account not found for username: " + username);
        }
        return account;
    }
}