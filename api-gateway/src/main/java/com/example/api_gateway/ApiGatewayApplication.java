package com.example.api_gateway;

import com.example.api_gateway.filter.JwtAuthGatewayFilterFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    // ✅ ON FORCE LE NOM DU BEAN ICI : "JwtAuth"
    // Cela lie directement le YAML (- name: JwtAuth) à cette méthode.
    @Bean(name = "JwtAuth")
    public JwtAuthGatewayFilterFactory jwtAuthGatewayFilterFactory() {
        return new JwtAuthGatewayFilterFactory();
    }
}