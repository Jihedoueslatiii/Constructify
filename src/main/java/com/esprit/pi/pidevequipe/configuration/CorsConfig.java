package com.esprit.pi.pidevequipe.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class CorsConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF (useful for stateless APIs)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Configure CORS

                // Configure authorization
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to specific endpoints without authentication
                        .requestMatchers(
                                "/swagger-ui/**",       // Swagger UI
                                "/v3/api-docs/**",     // OpenAPI JSON
                                "/swagger-resources/**", // Swagger resources
                                "/webjars/**",          // WebJars for Swagger
                                "/teams/create",
                                "/teams/all",
                                "/teams/teams/{id}",
                                "/teams/{teamId}/removeEmployee/{employeeId}",
                                "/teams/Delete/{teamId}",
                                "/teams/FindTeam/{teamId}",
                                "teams/paginationTeams",
                                "/api/ratings/add",
                                "/api/ratings/average",
                                "/api/ratings/**",
                                "/teams/suggest-name",
                                "/bot/chat",
                                "/error",
                                "/teams/{teamId}/addEmployee/{employeeId}"// Allow access to the /error page without authentication
                        ).permitAll()  // Allow access without authentication
                        .anyRequest().hasRole("Admin")

                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/error") // Handle access denied errors
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:63593"));// Allow Angular frontend
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Allow these HTTP methods
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-CSRF-TOKEN")); // Allow these headers
        config.setAllowCredentials(true); // Allow credentials (e.g., cookies)
        config.setMaxAge(3600L); // Cache preflight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply CORS to all endpoints
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Use BCrypt for password encoding
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}