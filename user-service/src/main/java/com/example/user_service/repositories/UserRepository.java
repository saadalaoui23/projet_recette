package com.example.user_service.repositories;

import com.example.user_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recherche un utilisateur par son adresse email.
     * Utilisé pour la connexion et la vérification d'unicité lors de l'enregistrement.
     * @param email L'email de l'utilisateur.
     * @return L'objet User ou null.
     */
    User findByEmail(String email);

    // NOTE: La méthode existsById(Long) est fournie par JpaRepository et utilisée par Feign.
}