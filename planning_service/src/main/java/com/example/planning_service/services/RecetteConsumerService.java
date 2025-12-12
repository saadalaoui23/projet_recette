package com.example.planning_service.services;

import com.example.planning_service.events.RecetteEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RecetteConsumerService {

    // Écoute le topic 'recettes-events' avec le 'group-id' défini dans la configuration
    @KafkaListener(topics = "recettes-events", groupId = "planning-service-group")
    public void handleRecetteEvent(RecetteEvent event) {

        System.out.println("--> Événement Kafka reçu dans le Service Planification:");
        System.out.println("    Recette ID: " + event.getId() + ", Nom: " + event.getNom() + ", Type: " + event.getEventType());

        // La logique de traitement des événements :
        switch (event.getEventType()) {
            case "CREATED":
            case "UPDATED":
                // ACTION : Mettre à jour le cache local de la recette pour faciliter les recherches
                // (ex: si le nom de la recette change, les menus doivent afficher le nouveau nom)
                System.out.println("    [Traitement] Mise à jour des informations de la recette dans le cache local.");
                // planningService.updateRecetteCache(event);
                break;

            case "DELETED":
                // ACTION : Marquer tous les menus utilisant cette recette comme "Invalides" ou "À Vérifier"
                System.out.println("    [Traitement] Recette supprimée. Vérification et potentielle invalidation des Menus.");
                // planningService.invalidateMenus(event.getId());
                break;

            default:
                System.err.println("    Type d'événement Kafka inconnu : " + event.getEventType());
        }
    }
}
