// planning-service/src/main/java/com/example/planning/events/RecetteEvent.java

package com.example.planning_service.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetteEvent {
    private Long id;
    private String nom;
    private String description;
    private String eventType;
}