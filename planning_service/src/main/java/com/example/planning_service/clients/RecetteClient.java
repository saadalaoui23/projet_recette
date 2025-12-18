// planning-service/src/main/java/com/example/planning/clients/RecetteClient.java

package com.example.planning_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.planning_service.dtos.RecetteDetailsResponse;

// FeignClient cible le Service Recettes
@FeignClient(
        name = "recettes-service",
        // En environnement Docker/K8s, on atteint le service par son nom DNS
        url = "http://recettes-app:8081"
)
public interface RecetteClient {

    // Contrat pour récupérer les détails d'une recette par ID
    // Back-end RecetteController est mappé sur @RequestMapping(\"/recettes\")
    // → l'URL effective est /recettes/{id}
    @GetMapping("/recettes/{id}")
    RecetteDetailsResponse getRecetteDetails(@PathVariable Long id);
}