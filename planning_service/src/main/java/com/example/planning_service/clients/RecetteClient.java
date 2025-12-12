// planning-service/src/main/java/com/example/planning/clients/RecetteClient.java

package com.example.planning_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.planning_service.dtos.RecetteDetailsResponse;

// FeignClient cible le Service Recettes
@FeignClient(
        name = "recettes-service",
        // URL fixe pour l'instant. Sera remplacée par le nom du service en Docker Compose.
        url = "http://localhost:8081"
)
public interface RecetteClient {

    // Contrat pour récupérer les détails d'une recette par ID
    // GET http://localhost:8081/api/recettes/{id}
    @GetMapping("/api/recettes/{id}")
    RecetteDetailsResponse getRecetteDetails(@PathVariable Long id);
}