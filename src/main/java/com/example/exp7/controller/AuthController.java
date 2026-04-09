package com.example.exp7.controller;

import com.example.exp7.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthController - Handles login and registration.
 *
 * Part (a): Login endpoint that returns JWT token on success.
 *
 * Uses HARDCODED users for simplicity:
 *   - admin / admin123  → role: ADMIN
 *   - user  / user123   → role: USER
 */
@RestController
public class AuthController {

    private final JwtUtil jwtUtil;

    // Hardcoded users (in real app, these come from database)
    private static final Map<String, String[]> USERS = new HashMap<>();

    static {
        // Format: username -> [password, role]
        USERS.put("admin", new String[]{"admin123", "ADMIN"});
        USERS.put("user",  new String[]{"user123",  "USER"});
    }

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * POST /login - Authenticate user and return JWT token.
     *
     * Request body (JSON):
     *   { "username": "admin", "password": "admin123" }
     *
     * Success response:
     *   { "token": "eyJhbGci...", "message": "Login successful!" }
     *
     * Failure response:
     *   { "error": "Invalid username or password!" }
     */
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        Map<String, String> response = new HashMap<>();

        // Check if user exists and password matches
        if (USERS.containsKey(username) && USERS.get(username)[0].equals(password)) {

            String role = USERS.get(username)[1];

            // Generate JWT token
            String token = jwtUtil.generateToken(username, role);

            response.put("token", token);
            response.put("message", "Login successful! Role: " + role);
            return response;
        }

        response.put("error", "Invalid username or password!");
        return response;
    }

    /**
     * POST /register - Simulated registration endpoint (public route).
     *
     * In a real app, this would save the user to a database.
     * Here it just returns a success message.
     */
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {

        Map<String, String> response = new HashMap<>();
        response.put("message", "User '" + request.get("username") + "' registered successfully!");
        response.put("note", "This is a demo. In a real app, this would save to a database.");
        return response;
    }
}
