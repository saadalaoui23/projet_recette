package com.example.user_service.services;

import com.example.user_service.dtos.RegisterRequest;
import com.example.user_service.models.User;

public interface UserService {

    // Pour l'API Feign du Service Recettes
    boolean userExists(Long userId);

    // Pour l'enregistrement des utilisateurs locaux
    User register(RegisterRequest request);

    // Pour la connexion des utilisateurs locaux
    String login(String email, String rawPassword);
}