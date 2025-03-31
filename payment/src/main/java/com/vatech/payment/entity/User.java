package com.vatech.payment.entity;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String roles;

    // Getter for 'id'
    public Long getId() {
        return id;
    }

    // Setter for 'id'
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for 'username'
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

    // Getter for 'roles'
    public String getRoles() {
        return roles;
    }

    // Setter for 'roles'
    public void setRoles(String roles) {
        this.roles = roles;
    }
}