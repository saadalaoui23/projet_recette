// com/DTO/RecetteRequest.java

package com.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// Optionnel: Vous pourriez ajouter jakarta.validation.constraints.NotBlank 
// pour valider que les champs ne sont pas vides, mais restons simple pour l'instant.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetteRequest {
    
    // Pas d'ID, car c'est une requête de création/mise à jour.
    
    private String nom;
    private String description;
    private String ingredients;
}