package com.example.user_service.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "app_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Champs d'identité complets
    private String firstName;
    private String lastName;

    // Email unique, souvent utilisé comme identifiant principal
    @Column(unique = true)
    private String email;

    // Le mot de passe hashé (pour les inscriptions traditionnelles)
    private String password;

    // Champ utilisé pour l'intégration future avec Google/OAuth2
    private String provider = "LOCAL"; // LOCAL, GOOGLE, etc.

    // Pour l'autorisation Spring Security
    private String role = "ROLE_USER";
}