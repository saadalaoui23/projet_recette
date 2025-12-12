package com.example.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class UserConfig {

    // ✅ C'est le Bean qui manquait et qui causait l'erreur !
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuration de sécurité basique pour permettre l'accès (à adapter selon vos besoins)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF pour les API REST
                .authorizeHttpRequests(auth -> auth
                        // Autoriser l'accès public à l'enregistrement et au login
                        .requestMatchers("/api/users/register", "/api/users/login", "/api/users/*/exists").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}