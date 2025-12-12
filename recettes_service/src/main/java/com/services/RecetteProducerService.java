package com.services;

import com.events.RecetteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecetteProducerService {

    private final KafkaTemplate<String, RecetteEvent> kafkaTemplate;

    // Le topic est injecté depuis application.yml
    @Value("${spring.kafka.topic.recette-event}")
    private String recetteTopic;

    public void sendRecetteEvent(RecetteEvent event) {
        // La clé du message (key) est l'ID de la recette.
        // Ceci garantit que toutes les mises à jour pour la même recette 
        // arrivent dans le même ordre sur le même partition.
        kafkaTemplate.send(recetteTopic, String.valueOf(event.getId()), event);

        System.out.println("Événement Kafka envoyé : Topic=" + recetteTopic + ", Type=" + event.getEventType() + ", Recette ID=" + event.getId());
    }
}