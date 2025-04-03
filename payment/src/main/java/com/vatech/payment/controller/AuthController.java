package com.vatech.payment.controller;

import com.vatech.payment.service.JwtUtil;
import com.vatech.payment.dto.AuthRequest;
import com.vatech.payment.service.UserService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.*;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = jwtUtil.generateToken(authRequest.getUsername());
//        System.out.println("Received request: " + authRequest.getUsername() + ", " + authRequest.getPassword());

        // Wrap token in a JSON object
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);

        return ResponseEntity.ok(response);
    }

    // New Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody AuthRequest authRequest) {
        String accountNumber = userService.registerUser(authRequest.getUsername(), authRequest.getPassword());

        // Wrap response in a Map for a more structured response
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("accountNumber", accountNumber); // Include account number in the response

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}