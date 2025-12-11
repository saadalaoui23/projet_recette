package com.example.planning_service.services;

import com.example.planning_service.dtos.MenuRequest;
import com.example.planning_service.dtos.MenuResponse;

import java.util.List;

public interface PlanningService {

    // CRUD de base
    MenuResponse createMenu(MenuRequest request);
    MenuResponse getMenuById(String id);
    List<MenuResponse> getAllMenus();
    MenuResponse updateMenu(String id, MenuRequest request);
    void deleteMenu(String id);

    // Logique métier spécifique

    // La méthode la plus complexe : elle doit appeler le Service Recettes
    // pour obtenir les ingrédients de toutes les recettes du menu.
    List<String> generateShoppingList(String menuId);
}