package com.vatech.payment.service;

import com.vatech.payment.entity.User;
import com.vatech.payment.entity.Transaction;
import com.vatech.payment.repository.UserRepository;
import com.vatech.payment.repository.AccountRepository;
import com.vatech.payment.repository.TransactionRepository;
import com.vatech.payment.entity.Account;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

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

    @Transactional
    public void deposit(String username, Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        // Fetch account details
        Account account = getAccountDetails(username);

        // Update account balance
        account.setAccountBalance(account.getAccountBalance() + amount);
        accountRepository.save(account); // Save changes

        // Log the deposit transaction
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType("DEPOSIT");
        transaction.setDate(new Date()); // Save current timestamp
        transactionRepository.save(transaction); // Save to Transaction table

    }

    @Transactional
    public void withdraw(String username, Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }

        // Fetch account details
        Account account = getAccountDetails(username);

        // Validate sufficient funds
        if (account.getAccountBalance() < amount) {
            throw new RuntimeException("Insufficient funds for withdrawal");
        }

        // Update account balance
        account.setAccountBalance(account.getAccountBalance() - amount);
        accountRepository.save(account); // Save changes

        // Log the withdrawal transaction
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType("WITHDRAWAL");
        transaction.setDate(new Date()); // Save current timestamp
        transactionRepository.save(transaction); // Save to Transaction table

    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository.findByAccountId(accountId); // Use accountId to fetch transactions
    }
}