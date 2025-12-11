package com.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.models.Recettes;

@Repository
public interface RecetteRepository extends JpaRepository<Recettes, Long>{
    
}
