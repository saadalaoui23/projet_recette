package com.services;

import java.util.List;

import org.springframework.stereotype.Service;
import com.DTO.RecetteRequest;
import com.exception.RnotFound;
import com.models.Recettes;
import com.repositories.RecetteRepository;

import lombok.RequiredArgsConstructor;

// NOUVEAUX IMPORTS :
import com.events.RecetteEvent; // Votre DTO d'événement Kafka
import com.clients.UserClient; // Votre Feign Client hypothétique

@Service
@RequiredArgsConstructor
public class RecetteServiceImpl implements RecetteService {

    // Injection par Constructeur (Best Practice)
    private final RecetteRepository recetteRepository;

    // NOUVEAU : Injection du Kafka Producer
    private final RecetteProducerService producerService;

    // NOUVEAU : Injection du Feign Client (pour UserService hypothétique)
    private final UserClient userClient;

    // --- 1. READ ALL ---
    @Override
    public List<Recettes> getallrecettes() {
        return recetteRepository.findAll();
    }

    // --- 2. READ BY ID ---
    @Override
    public Recettes getrecettebyid(Long id) {
        return recetteRepository.findById(id)
                .orElseThrow(() -> new RnotFound("Recette non trouvée avec l'ID: " + id));
    }

    // --- 3. CREATE ---
    @Override
    public Recettes createrecette(RecetteRequest recetteRequest) {

        // **********************************************
        // NOUVEAU : 1. VALIDATION SYNCHRONE (Feign)
        // Supposons que RecetteRequest inclut un Long userId pour la validation
        /*
        if (recetteRequest.getUserId() != null) {
            try {
                if (!userClient.checkUserExists(recetteRequest.getUserId())) {
                    throw new RnotFound("L'utilisateur spécifié n'existe pas. Création annulée.");
                }
            } catch (Exception e) {
                System.err.println("Erreur Feign: Impossible de valider l'utilisateur. " + e.getMessage());
                // On pourrait ajouter une logique pour bloquer ou autoriser la création ici
            }
        }
        */
        // **********************************************

        Recettes nouvelleRecette = mapRequestToRecette(recetteRequest);
        Recettes savedRecette = recetteRepository.save(nouvelleRecette);

        // **********************************************
        // NOUVEAU : 2. PUBLICATION KAFKA (Asynchrone)
        producerService.sendRecetteEvent(new RecetteEvent(
                savedRecette.getId(),
                savedRecette.getNom(),
                savedRecette.getDescription(),
                "CREATED"
        ));
        // **********************************************

        return savedRecette;
    }

    // --- 4. UPDATE ---
    @Override
    public Recettes updaterecette(Long id, RecetteRequest recetteRequest) {
        Recettes existingRecette = recetteRepository.findById(id)
                .orElseThrow(() -> new RnotFound("Impossible de mettre à jour. Recette non trouvée avec l'ID: " + id));

        existingRecette.setNom(recetteRequest.getNom());
        existingRecette.setDescription(recetteRequest.getDescription());
        existingRecette.setIngredients(recetteRequest.getIngredients());

        Recettes updatedRecette = recetteRepository.save(existingRecette);

        // **********************************************
        // NOUVEAU : PUBLICATION KAFKA (Asynchrone)
        producerService.sendRecetteEvent(new RecetteEvent(
                updatedRecette.getId(),
                updatedRecette.getNom(),
                updatedRecette.getDescription(),
                "UPDATED"
        ));
        // **********************************************

        return updatedRecette;
    }

    // --- 5. DELETE ---
    @Override
    public void deleterecette(Long id) {
        Recettes recetteToDelete = recetteRepository.findById(id)
                .orElseThrow(() -> new RnotFound("Impossible de supprimer. Recette non trouvée avec l'ID: " + id));

        recetteRepository.delete(recetteToDelete);

        // **********************************************
        // NOUVEAU : PUBLICATION KAFKA (Asynchrone)
        producerService.sendRecetteEvent(new RecetteEvent(
                id,
                recetteToDelete.getNom(),
                recetteToDelete.getDescription(),
                "DELETED"
        ));
        // **********************************************
    }

    // --- Méthode de Mapping Privée ---

    private Recettes mapRequestToRecette(RecetteRequest request) {
        return new Recettes(
                null, // ID est laissé null pour l'auto-incrémentation
                request.getNom(),
                request.getDescription(),
                request.getIngredients()
        );
    }
}