// com/example/planning/services/PlanningServiceImpl.java

package com.example.planning_service.services;

import com.example.planning_service.dtos.MenuRequest;
import com.example.planning_service.dtos.MenuResponse;
import com.example.planning_service.model.Menu;
import com.example.planning_service.repositories.MenuRepository;
import com.example.planning_service.services.PlanningService;
import com.example.planning_service.exceptions.RnotFound;

// NOUVEAUX IMPORTS POUR FEIGN
import com.example.planning_service.clients.RecetteClient;
import com.example.planning_service.dtos.RecetteDetailsResponse;
import com.example.planning_service.model.RepasEntry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList; // Pour la liste d'achats

@Service
@RequiredArgsConstructor
public class PlanningServiceImpl implements PlanningService {

    private final MenuRepository menuRepository;

    // NOUVEAU : Injection du Feign Client pour appeler le Service Recettes
    private final RecetteClient recetteClient;

    // --- MAPPING (Logique de conversion DTO <-> Entité) ---

    private Menu mapRequestToMenu(MenuRequest request) {
        // ID laissé à null pour la création
        return new Menu(
                null,
                request.getNom(),
                request.getPlanningHebdomadaire(),
                request.getUtilisateurId()
        );
    }

    private MenuResponse mapMenuToResponse(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getNom(),
                menu.getPlanningHebdomadaire(),
                menu.getUtilisateurId()
        );
    }

    // --- 1. CREATE ---
    @Override
    public MenuResponse createMenu(MenuRequest request) {
        Menu nouveauMenu = mapRequestToMenu(request);
        Menu savedMenu = menuRepository.save(nouveauMenu);
        return mapMenuToResponse(savedMenu);
    }

    // --- 2. READ ALL ---
    @Override
    public List<MenuResponse> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        // Utilisation du stream pour mapper chaque entité en DTO de réponse
        return menus.stream()
                .map(this::mapMenuToResponse)
                .collect(Collectors.toList());
    }

    // --- 3. READ BY ID ---
    @Override
    public MenuResponse getMenuById(String id) {
        Menu menu = menuRepository.findById(id)
                // Lancer l'exception si le menu n'est pas trouvé
                .orElseThrow(() -> new RnotFound("Menu non trouvé avec l'ID: " + id));
        return mapMenuToResponse(menu);
    }

    // --- 4. UPDATE ---
    @Override
    public MenuResponse updateMenu(String id, MenuRequest request) {
        Menu existingMenu = menuRepository.findById(id)
                // Lancer l'exception si le menu n'existe pas
                .orElseThrow(() -> new RnotFound("Impossible de mettre à jour. Menu non trouvé avec l'ID: " + id));

        // Mise à jour des champs à partir du Request DTO
        existingMenu.setNom(request.getNom());
        existingMenu.setPlanningHebdomadaire(request.getPlanningHebdomadaire());
        existingMenu.setUtilisateurId(request.getUtilisateurId());

        Menu updatedMenu = menuRepository.save(existingMenu);
        return mapMenuToResponse(updatedMenu);
    }

    // --- 5. DELETE ---
    @Override
    public void deleteMenu(String id) {
        if (!menuRepository.existsById(id)) {
            // Lancer l'exception si le menu n'existe pas
            throw new RnotFound("Impossible de supprimer. Menu non trouvé avec l'ID: " + id);
        }
        menuRepository.deleteById(id);
    }

    // --- 6. LOGIQUE MÉTIER SPÉCIFIQUE : Génération de la liste de courses ---

    @Override
    public List<String> generateShoppingList(String menuId) {
        // 1. Récupérer le Menu
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RnotFound("Menu non trouvé avec l'ID: " + menuId));

        List<String> shoppingList = new ArrayList<>();

        // 2. Collecter les IDs de recettes uniques du menu
        List<Long> recetteIds = menu.getPlanningHebdomadaire().values().stream()
                .flatMap(List::stream) // Aplatir la Map des listes de repas en une liste de RepasEntry
                .map(RepasEntry::getRecetteId) // Récupérer l'ID (qui est un String)
                .map(Long::valueOf) // Convertir en Long (pour le Feign Client)
                .distinct() // Ne récupérer qu'une seule fois chaque recette
                .collect(Collectors.toList());

        // 3. Appeler le Service Recettes pour chaque ID via Feign
        for (Long id : recetteIds) {
            try {
                // APPEL SYNCHRONE VIA FEIGN
                RecetteDetailsResponse details = recetteClient.getRecetteDetails(id);

                // 4. Ajouter les ingrédients à la liste globale
                if (details.getIngredients() != null) {
                    shoppingList.addAll(details.getIngredients());
                }
            } catch (Exception e) {
                // Gestion des erreurs (ex: Recette non trouvée ou Service Recettes HS)
                System.err.println("Erreur Feign lors de la récupération de la recette ID " + id + ": " + e.getMessage());
                // Ajouter un marqueur dans la liste pour alerter l'utilisateur
                shoppingList.add("ERREUR: Ingrédients manquants pour la recette ID " + id);
            }
        }

        // 5. Retourner la liste d'achats agrégée
        return shoppingList;
    }
}