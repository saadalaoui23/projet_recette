package com.example.planning_service.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepasEntry {

    // Identifiant du Service Recettes
    private String recetteId;

    // Ex: "Petit-déjeuner", "Déjeuner", "Dîner"
    private String typeRepas;
}