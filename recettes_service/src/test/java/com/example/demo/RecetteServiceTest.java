package com.example.demo; // Assurez-vous que le package correspond à la structure de vos services

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows; // Importez votre exception
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.DTO.RecetteRequest;
import com.exception.RnotFound;
import com.models.Recettes;
import com.repositories.RecetteRepository;
import com.services.RecetteServiceImpl;

// Indique que Mockito doit être utilisé avec JUnit 5
@ExtendWith(MockitoExtension.class) 
class RecetteServiceTest {

    // Crée une instance de RecetteServiceImpl et injecte les mocks (@Mock) dedans
    @InjectMocks
    private RecetteServiceImpl recetteService;

    // Crée une instance simulée (mock) du Repository
    @Mock
    private RecetteRepository recetteRepository;


    // --- Test 1: Récupérer une recette existante ---
    @Test
    void getRecetteById_shouldReturnRecette_whenIdExists() {
        // ARRANGE (Préparation)
        Long idExistant = 1L;
        Recettes recette = new Recettes();
        // Simuler le comportement du Repository: si findById(1L) est appelé, il retourne l'Optional avec la recette.
        when(recetteRepository.findById(idExistant)).thenReturn(Optional.of(recette));

        // ACT (Action)
        Recettes resultat = recetteService.getrecettebyid(idExistant);

        // ASSERT (Vérification)
        assertNotNull(resultat);
        assertEquals(recette, resultat);
        // Vérifier que findById a bien été appelé une fois
        verify(recetteRepository, times(1)).findById(idExistant);
    }

    // --- Test 2: Gérer la recette non trouvée et lancer l'exception ---
    @Test
    void getRecetteById_shouldThrowRnotFound_whenIdDoesNotExist() {
        // ARRANGE (Préparation)
        Long idInexistant = 99L;
        // Simuler le Repository: si findById(99L) est appelé, il retourne un Optional vide.
        when(recetteRepository.findById(idInexistant)).thenReturn(Optional.empty());

        // ACT & ASSERT (Vérification de l'exception)
        // Vérifie qu'appeler la méthode lance bien l'exception RnotFound
        assertThrows(RnotFound.class, () -> {
            recetteService.getrecettebyid(idInexistant);
        });
        
        verify(recetteRepository, times(1)).findById(idInexistant);
    }
    
    // --- Test 3: Tester la suppression ---
    @Test
    void deleteRecette_shouldDeleteRecette_whenIdExists() {
        // ARRANGE
        Long idASupprimer = 2L;
        // Simuler que la recette existe
        when(recetteRepository.existsById(idASupprimer)).thenReturn(true);

        // ACT
        recetteService.deleterecette(idASupprimer);

        // ASSERT
        // Vérifie que deleteById a été appelé une seule fois
        verify(recetteRepository, times(1)).deleteById(idASupprimer);
    }

    // --- Test 4: Tester la création ---
    @Test
    void createRecette_shouldSaveAndReturnRecette() {
        // ARRANGE
        RecetteRequest request = new RecetteRequest(/* ... données de la requête ... */);
        Recettes recetteASauver = new Recettes(/* ... entité mappée ... */);
        Recettes recetteSauvee = new Recettes(/* ... entité avec ID ... */);
        
        // Simuler le Repository: quand save() est appelé, il retourne l'entité
        when(recetteRepository.save(any(Recettes.class))).thenReturn(recetteSauvee);
        
        // ACT (ATTENTION: Ceci nécessite que vous implémentiez le mapping DTO->Entité dans le service !)
        // Recettes resultat = recetteService.createrecette(request);
        
        // ASSERT
        // assertEquals(recetteSauvee, resultat);
        // verify(recetteRepository, times(1)).save(any(Recettes.class));
    }
}