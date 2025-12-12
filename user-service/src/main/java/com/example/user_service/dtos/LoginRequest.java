package com.example.user_service.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password; // Mot de passe non hashé
}