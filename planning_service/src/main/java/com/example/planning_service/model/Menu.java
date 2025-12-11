package com.example.planning_service.model;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.DayOfWeek;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "menus") // Annotation spécifique MongoDB
public class Menu {

    // Utilisation de String pour l'ID de MongoDB
    @Id
    private String id;

    private String nom;

    // Mappe chaque jour de la semaine à une liste de repas pour ce jour
    // Exemple: { "MONDAY": [RepasEntry, RepasEntry], "TUESDAY": [...] }
    private Map<DayOfWeek, List<RepasEntry>> planningHebdomadaire;

    private String utilisateurId; // Si nous avions un Service Utilisateur
}
