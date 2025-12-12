package com.example.user_service.dtos;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password; // Mot de passe non hashé entrant
}