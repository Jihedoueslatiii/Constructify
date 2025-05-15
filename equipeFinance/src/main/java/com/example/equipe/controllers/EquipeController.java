package com.example.equipe.controllers;

import com.example.equipe.entities.Equipe;
import com.example.equipe.services.EquipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipe")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Equipe Controller", description = "Gestion des equipes")
public class EquipeController {

    private final EquipeService equipeService;
    @Operation(summary = "get equipe du projet")
    @GetMapping("/getByProjet/{projectId}")

    public List<Equipe> getEquipesByProject(@PathVariable int projectId) {
        List<Equipe> equipes = equipeService.findEquipesByProjectId(projectId);

        if (equipes.isEmpty()) {
            throw new RuntimeException("Aucune équipe trouvée pour le projet ID: " + projectId);
        }

        return equipes;
    }
}
