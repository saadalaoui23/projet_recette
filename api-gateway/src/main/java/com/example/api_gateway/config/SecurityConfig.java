package com.example.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Active CORS
                .csrf(csrf -> csrf.disable()) // Désactive CSRF pour les API REST
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll() // Permet tout (JWT géré par filtre custom)
                )
                .build();
    }

    /**
     * ✅ Configuration CORS complète pour la Gateway
     * Permet au frontend (localhost:5173) de communiquer avec la Gateway
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // ✅ ORIGINE AUTORISÉE (Frontend React)
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));

        // ✅ MÉTHODES HTTP AUTORISÉES
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // ✅ HEADERS AUTORISÉS (Frontend → Backend)
        corsConfig.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        // ✅ HEADERS EXPOSÉS (Backend → Frontend)
        corsConfig.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Total-Count"
        ));

        // ✅ AUTORISE LES COOKIES/CREDENTIALS
        corsConfig.setAllowCredentials(true);

        // ✅ DURÉE DU CACHE PREFLIGHT (1 heure)
        corsConfig.setMaxAge(3600L);

        // Applique cette config à tous les endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }
}