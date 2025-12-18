package com.example.planning_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.planning_service.dtos.RecetteDetailsResponse;

/**
 * FeignClient pour la communication interne entre Planning et Recettes.
 * Il ne passe pas par la Gateway (plus performant en interne Docker).
 */
@FeignClient(
        name = "recette-service",
        url = "http://recettes-app:8081" // ✅ Cible le conteneur du microservice Recettes
)
public interface RecetteClient {

    /**
     * Récupère les détails complets d'une recette.
     * @param id Identifiant de la recette stocké dans MongoDB (Planning)
     * @return Les détails venant de PostgreSQL (Recettes)
     */
    @GetMapping("/recettes/{id}")
    RecetteDetailsResponse getRecetteDetails(@PathVariable("id") Long id); // ✅ "id" ajouté pour la stabilité
}