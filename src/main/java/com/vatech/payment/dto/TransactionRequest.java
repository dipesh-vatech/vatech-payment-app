package com.vatech.payment.dto;

public class TransactionRequest {
    private Double amount; // Amount to deposit/withdraw
    private String accountNumber; // Associated account number for the transaction

    // Getter and Setter for amount
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    // Getter and Setter for accountNumber
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}