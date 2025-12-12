// recettes-service/src/main/java/com/example.recettes_service/clients/UserClient.java

package com.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Nous supposons que le Service Utilisateur tourne sur le port 8083
@FeignClient(
        name = "user-service",
        url = "http://localhost:8083"
)
public interface UserClient {

    // Contrat pour vérifier si un ID utilisateur existe
    // GET http://localhost:8083/api/users/{userId}/exists
    @GetMapping("/api/users/{userId}/exists")
    boolean checkUserExists(@PathVariable Long userId);
}