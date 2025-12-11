package com.example.planning_service.dtos;

import com.example.planning_service.model.RepasEntry;
import lombok.Data;

import java.util.List;
import java.time.DayOfWeek;
import java.util.Map;

@Data
public class MenuRequest {

    // Pas d'ID
    private String nom;

    // Le mapping complet des repas pour les jours (Map<Jour, List<Repas>>)
    private Map<DayOfWeek, List<RepasEntry>> planningHebdomadaire;

    // L'ID de l'utilisateur concerné
    private String utilisateurId;
}