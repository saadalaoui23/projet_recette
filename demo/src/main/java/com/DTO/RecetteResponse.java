// com/DTO/RecetteResponse.java

package com.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetteResponse {
    
    // L'ID est inclus pour que le client puisse référencer la recette.
    private Long id;
    
    private String nom;
    private String description;
    private String ingredients;
}