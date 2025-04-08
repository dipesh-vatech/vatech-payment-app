package com.vatech.payment.dto;

public class TransactionRequest {
    private Double amount; // Amount to deposit/withdraw

    // Getters and setters
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}