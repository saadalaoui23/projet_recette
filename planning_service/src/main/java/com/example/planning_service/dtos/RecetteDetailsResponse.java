// planning-service/src/main/java/com/example/planning/clients/RecetteDetailsResponse.java

package com.example.planning_service.dtos;

import lombok.Data;
import java.util.List;

@Data
public class RecetteDetailsResponse {
    private Long id;
    private String nom;
    private String description;

    // L'information cruciale pour la liste de courses
    private List<String> ingredients;
}