package com.services; 

import java.util.List;

import org.springframework.stereotype.Service; // Pour getrecettebyid

import com.DTO.RecetteRequest;
import com.exception.RnotFound;
import com.models.Recettes; // Votre exception pour 404
import com.repositories.RecetteRepository; // Votre entité

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecetteServiceImpl implements RecetteService {
    
    // Injection par Constructeur (Best Practice)
    private final RecetteRepository recetteRepository; 

    // --- 1. READ ALL ---
    @Override
    public List<Recettes> getallrecettes() {
        // Implémentation Complète
        return recetteRepository.findAll();
    }

    // --- 2. READ BY ID ---
    @Override
    public Recettes getrecettebyid(Long id) {
        // Implémentation Complète avec orElseThrow
        return recetteRepository.findById(id)
                .orElseThrow(() -> new RnotFound("Recette non trouvée avec l'ID: " + id));
    }

    // --- 3. CREATE ---
    @Override
    public Recettes createrecette(RecetteRequest recetteRequest) {
        // Implémentation Complète avec Mapping DTO -> Entité
        
        // 1. Mapper le Request DTO à l'Entité BDD
        Recettes nouvelleRecette = mapRequestToRecette(recetteRequest);
        
        // 2. Sauvegarder et retourner l'entité avec son ID généré
        return recetteRepository.save(nouvelleRecette);
    }

    // --- 4. UPDATE ---
    @Override
    public Recettes updaterecette(Long id, RecetteRequest recetteRequest) {
        // 1. Chercher l'entité, ou lancer une exception (RnotFound)
        Recettes existingRecette = recetteRepository.findById(id)
                .orElseThrow(() -> new RnotFound("Impossible de mettre à jour. Recette non trouvée avec l'ID: " + id));

        // 2. Mettre à jour les champs de l'entité existante (Mapping)
        existingRecette.setNom(recetteRequest.getNom());
        existingRecette.setDescription(recetteRequest.getDescription());
        existingRecette.setIngredients(recetteRequest.getIngredients());
        
        // 3. Sauvegarder et retourner l'entité mise à jour
        return recetteRepository.save(existingRecette);
    }
    
    // --- 5. DELETE ---
    @Override
    public void deleterecette(Long id) {
        // Implémentation Complète avec vérification d'existence
        if (!recetteRepository.existsById(id)) {
            throw new RnotFound("Impossible de supprimer. Recette non trouvée avec l'ID: " + id);
        }
        recetteRepository.deleteById(id);
    }

    // --- Méthode de Mapping Privée ---

    // Mappe le DTO de Requête vers l'Entité BDD (Utilisé dans CREATE)
    private Recettes mapRequestToRecette(RecetteRequest request) {
        // Utilise le constructeur avec tous les arguments (sauf l'ID, qui est null pour la création)
        return new Recettes(
            null, // ID est laissé null pour l'auto-incrémentation
            request.getNom(),
            request.getDescription(),
            request.getIngredients()
        );
    }
}