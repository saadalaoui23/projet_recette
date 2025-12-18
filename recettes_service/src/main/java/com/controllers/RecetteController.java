package com.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.DTO.RecetteRequest;
import com.models.Recettes;
import com.services.RecetteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/recettes", "/recettes/"})
@RequiredArgsConstructor
public class RecetteController {
    private final RecetteService recetteService; // Injection automatique grâce à @RequiredArgsConstructor

    // 1. READ ALL : GET /api/recettes
    @GetMapping
    @ResponseStatus(HttpStatus.OK) // Retourne le code 200 OK
    public List<Recettes> getAllRecettes() {
        return recetteService.getallrecettes();
    }

    // 2. READ BY ID : GET /api/recettes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Recettes> getRecetteById(@PathVariable Long id) {
        // Le Service lance RnotFound si non trouvé. Si ça passe, c'est un succès (200 OK).
        Recettes recette = recetteService.getrecettebyid(id); 
        return ResponseEntity.ok(recette); // Retourne 200 OK avec le corps de la recette.
    }

    // 3. CREATE : POST /api/recettes
    @PostMapping
    // @RequestBody mappe le JSON entrant à l'objet RecetteRequest
    // ResponseEntity permet de définir le statut de la réponse (201 CREATED)
    public ResponseEntity<Recettes> createRecette(@RequestBody RecetteRequest recetteRequest) {
        Recettes nouvelleRecette = recetteService.createrecette(recetteRequest);
        return new ResponseEntity<>(nouvelleRecette, HttpStatus.CREATED); // Retourne 201 Created
    }

    // 4. UPDATE : PUT /api/recettes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Recettes> updateRecette(@PathVariable Long id, 
                                                 @RequestBody RecetteRequest recetteRequest) {
        // Le Service lance RnotFound si non trouvé. Si ça passe, c'est un succès (200 OK).
        Recettes updatedRecette = recetteService.updaterecette(id, recetteRequest);
        
        // La gestion d'erreur (404) est faite par l'exception RnotFound du Service.
        return ResponseEntity.ok(updatedRecette); // Retourne 200 OK.
    }

    // 5. DELETE : DELETE /api/recettes/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retourne 204 No Content (succès sans corps de réponse)
    public void deleteRecette(@PathVariable Long id) {
        recetteService.deleterecette(id);
    }
}
