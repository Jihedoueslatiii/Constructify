package com.esprit.pi.constructify.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Désactiver CSRF (utile pour les API stateless)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Configurer CORS

                // Configurer les autorisations
                .authorizeHttpRequests(auth -> auth
                        // Autoriser l'accès public à tous les endpoints sans authentification
                        .requestMatchers(
                                "/swagger-ui/**",       // Swagger UI
                                "/v3/api-docs/**",     // OpenAPI JSON
                                "/swagger-resources/**", // Swagger resources
                                "/webjars/**",          // WebJars pour Swagger
                                "/user/register",       // Endpoint d'inscription
                                "/user/all",           // Lister tous les utilisateurs
                                "/userById/{id}",           // Obtenir un utilisateur par ID
                                "/user/user/{id}/role",
                                "/Constructify/user/{id}/role"
                        ).permitAll()  // Permet l'accès sans authentification
                        .anyRequest().permitAll()  // Permet toutes les autres routes sans authentification
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        //config.setAllowedOrigins(Arrays.asList("http://localhost:62530"));// Permet Angular
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-CSRF-TOKEN"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Utilisation de BCrypt pour les mots de passe
    }
}
