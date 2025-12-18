package com.example.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class UserConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ❌ CORS n'est pas nécessaire ici : le navigateur ne parle JAMAIS
                // directement au user-service, seulement à la Gateway.
                // On le désactive donc pour éviter les headers CORS doublons.
                .cors(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf.disable()) // Désactive CSRF pour les API REST
                .authorizeHttpRequests(auth -> auth
                        // Routes publiques (register, login, exists)
                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login",
                                "/api/users/*/exists"
                        ).permitAll()
                        // Toutes les autres routes nécessitent une authentification
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    // ❌ Plus de bean CORS ici : la Gateway est la seule à gérer CORS
}