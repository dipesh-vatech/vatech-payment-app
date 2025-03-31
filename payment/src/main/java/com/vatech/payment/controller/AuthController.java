package com.vatech.payment.controller;

import com.vatech.payment.service.JwtUtil;
import com.vatech.payment.dto.AuthRequest;
import com.vatech.payment.service.UserService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authRequest.getUsername());
        System.out.println("Received request: " + authRequest.getUsername() + ", " + authRequest.getPassword());
        return ResponseEntity.ok(jwt);
    }

    // New Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        String response = userService.registerUser(authRequest.getUsername(), authRequest.getPassword());
        return ResponseEntity.ok(response);
    }

}