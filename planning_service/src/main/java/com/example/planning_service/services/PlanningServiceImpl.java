// com/example/planning/services/PlanningServiceImpl.java

package com.example.planning_service.services;

import com.example.planning_service.dtos.MenuRequest;
import com.example.planning_service.dtos.MenuResponse;
import com.example.planning_service.model.Menu;
import com.example.planning_service.repositories.MenuRepository;
import com.example.planning_service.services.PlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Assurez-vous d'utiliser votre package d'exceptions
import com.example.planning_service.exceptions.RnotFound;

@Service
@RequiredArgsConstructor
public class PlanningServiceImpl implements PlanningService {

    private final MenuRepository menuRepository;

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

    // --- 6. LOGIQUE MÉTIER SPÉCIFIQUE (à implémenter plus tard) ---

    @Override
    public List<String> generateShoppingList(String menuId) {
        // Cette méthode nécessitera un appel HTTP (Feign Client) au Service Recettes
        // pour récupérer les ingrédients de chaque recette listée dans le menu.

        System.out.println("LOGIQUE Kafka/Feign : Demande de liste d'achats pour le Menu ID: " + menuId);

        // Pour l'instant, nous renvoyons une liste vide.
        return List.of("Implémentation Feign Client nécessaire ici!");
    }
}