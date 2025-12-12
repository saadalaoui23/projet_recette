package com.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Doit être sérialisable en JSON
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetteEvent {
    private Long id;
    private String nom;
    private String description;
    // Type d'événement : "CREATED", "UPDATED", "DELETED"
    private String eventType;
}