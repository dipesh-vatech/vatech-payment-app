package com.vatech.payment.dto;

public class AuthRequest {
    private String username;
    private String password;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    // Setter for 'username'
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for 'password'
    public String getPassword() {
        return password;
    }

    // Setter for 'password'
    public void setPassword(String password) {
        this.password = password;
    }
}