package com.services;
import java.util.List;

import com.DTO.RecetteRequest;
import com.models.Recettes;

public interface RecetteService {
    List<Recettes> getallrecettes();
    Recettes getrecettebyid(Long id);
    Recettes createrecette(RecetteRequest recetteRequest);
    Recettes updaterecette(Long id, RecetteRequest recetteRequest);  
    void deleterecette(Long id);
}
