package com.example.exp7.config;

import com.example.exp7.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig - Configures Spring Security for JWT authentication.
 *
 * Key things configured here:
 *   1. Disable CSRF (not needed for stateless JWT)
 *   2. Allow /login and /register without authentication (public routes)
 *   3. All other endpoints require authentication
 *   4. Add our JwtFilter before Spring's default username/password filter
 *   5. Enable @PreAuthorize annotations for role-based access
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // enables @PreAuthorize annotation
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF (we use JWT tokens, not cookies)
            .csrf(csrf -> csrf.disable())

            // 2. Define which URLs are public vs protected
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/index.html").permitAll()  // public routes
                .anyRequest().authenticated()                        // everything else needs JWT
            )

            // 3. Stateless session (no server-side sessions, pure JWT)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 4. Add our JWT filter before the default auth filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
