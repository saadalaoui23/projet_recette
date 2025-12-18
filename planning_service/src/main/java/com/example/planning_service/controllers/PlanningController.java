// com/example/planning/controllers/PlanningController.java

package com.example.planning_service.controllers;

import com.example.planning_service.dtos.MenuRequest;
import com.example.planning_service.dtos.MenuResponse;
import com.example.planning_service.services.PlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/menus", "/menus/"}) // Endpoint de base pour les menus
@RequiredArgsConstructor
public class PlanningController {

    private final PlanningService planningService;

    // --- 1. READ ALL : GET /api/planning/menus
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MenuResponse> getAllMenus() {
        return planningService.getAllMenus();
    }

    // --- 2. READ BY ID : GET /api/planning/menus/{id}
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MenuResponse getMenuById(@PathVariable String id) {
        // Le Service gère l'exception RnotFound (qui se traduit en 404 NOT FOUND)
        return planningService.getMenuById(id);
    }

    // --- 3. CREATE : POST /api/planning/menus
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Retourne le code 201 Created
    public MenuResponse createMenu(@RequestBody MenuRequest request) {
        return planningService.createMenu(request);
    }

    // --- 4. UPDATE : PUT /api/planning/menus/{id}
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MenuResponse updateMenu(@PathVariable String id, @RequestBody MenuRequest request) {
        // Le Service gère l'exception RnotFound (qui se traduit en 404 NOT FOUND)
        return planningService.updateMenu(id, request);
    }

    // --- 5. DELETE : DELETE /api/planning/menus/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retourne le code 204 No Content (succès sans corps)
    public void deleteMenu(@PathVariable String id) {
        planningService.deleteMenu(id);
    }

    // --- 6. LOGIQUE MÉTIER SPÉCIFIQUE : GET /api/planning/menus/{id}/shopping-list
    // Note: L'implémentation de ce service est incomplète (nécessite Feign/Kafka)
    @GetMapping("/{id}/shopping-list")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getShoppingList(@PathVariable String id) {
        return planningService.generateShoppingList(id);
    }
}