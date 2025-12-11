package com.example.planning_service.repositories;

import com.example.planning_service.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Le Repository doit hériter de MongoRepository, spécifiant l'Entité (Menu) et le type d'ID (String)
public interface MenuRepository extends MongoRepository<Menu, String> {

    // Exemple de requête personnalisée basée sur le nom (implémentée automatiquement par Spring Data)
    List<Menu> findByNomContainingIgnoreCase(String nom);

    // Vous pouvez ajouter d'autres méthodes de recherche ici si nécessaire
}
