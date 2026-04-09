package com.example.exp7.filter;

import com.example.exp7.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JwtFilter - Runs BEFORE every request to check for a valid JWT token.
 *
 * How it works:
 *   1. Check if request has "Authorization: Bearer <token>" header
 *   2. If yes, validate the token
 *   3. If valid, tell Spring Security "this user is authenticated"
 *   4. If no token or invalid, let the request continue (Spring Security
 *      will block it if the endpoint requires authentication)
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Get the Authorization header
        String authHeader = request.getHeader("Authorization");

        // Step 2: Check if it starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7); // remove "Bearer " prefix

            // Step 3: Validate the token
            if (jwtUtil.isTokenValid(token)) {

                String username = jwtUtil.getUsername(token);
                String role = jwtUtil.getRole(token);

                // Step 4: Create authentication object with role
                // "ROLE_" prefix is required by Spring Security
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                username,   // principal (who)
                                null,       // credentials (not needed, we have token)
                                List.of(authority)  // authorities (roles)
                        );

                // Step 5: Set authentication in Spring Security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue with the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
