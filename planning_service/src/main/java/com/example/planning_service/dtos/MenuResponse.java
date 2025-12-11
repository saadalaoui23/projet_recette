package com.example.planning_service.dtos;

import com.example.planning_service.model.RepasEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse {

    // L'ID est inclus en sortie
    private String id;

    private String nom;

    private Map<DayOfWeek, List<RepasEntry>> planningHebdomadaire;

    private String utilisateurId;
}