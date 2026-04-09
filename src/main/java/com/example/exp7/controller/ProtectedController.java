package com.example.exp7.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * ProtectedController - Endpoints that require authentication and specific roles.
 *
 * Part (b): Protected endpoints that need valid JWT token.
 * Part (c): Role-Based Access Control (RBAC) using @PreAuthorize.
 *
 * Endpoints:
 *   GET /dashboard     → any authenticated user (ADMIN or USER)
 *   GET /admin/data    → only ADMIN role
 *   GET /user/profile  → only USER role
 */
@RestController
public class ProtectedController {

    /**
     * GET /dashboard - Accessible by ANY authenticated user.
     *
     * Both ADMIN and USER can access this.
     * The JWT token must be sent in the Authorization header.
     */
    @GetMapping("/dashboard")
    public Map<String, String> dashboard(Authentication auth) {

        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to the Dashboard!");
        response.put("user", auth.getName());
        response.put("authorities", auth.getAuthorities().toString());
        return response;
    }

    /**
     * GET /admin/data - ONLY accessible by ADMIN role.
     *
     * @PreAuthorize checks the role BEFORE the method runs.
     * If the user doesn't have ADMIN role, they get 403 Forbidden.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/data")
    public Map<String, String> adminData(Authentication auth) {

        Map<String, String> response = new HashMap<>();
        response.put("message", "This is ADMIN-only data!");
        response.put("user", auth.getName());
        response.put("secretData", "Admin secret: Server metrics are healthy.");
        return response;
    }

    /**
     * GET /user/profile - ONLY accessible by USER role.
     *
     * @PreAuthorize checks the role BEFORE the method runs.
     * If the user doesn't have USER role, they get 403 Forbidden.
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/profile")
    public Map<String, String> userProfile(Authentication auth) {

        Map<String, String> response = new HashMap<>();
        response.put("message", "This is your USER profile!");
        response.put("user", auth.getName());
        response.put("profileInfo", "Regular user dashboard with basic features.");
        return response;
    }
}
