package com.example.projecttask.controllers;

import com.example.projecttask.entities.Project;
import com.example.projecttask.repositories.ProjectRepository;
import com.example.projecttask.services.EquipeClient;
import com.example.projecttask.services.IProjectService;
import com.example.projecttask.services.ProjectService;
import com.example.projecttask.services.RessourceClient;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final IProjectService projectService;
    private final ProjectRepository projectRepository;
    private final EquipeClient equipeClient;
    private final RessourceClient ressourceClient;
    ///
    @Operation(summary = "Lister tous les projets")
    @GetMapping("/getAllProjets")
    public ResponseEntity<List<Project>> getAllProjets() {  //
        List<Project> ressources = projectService.getAllProjets();
        return ResponseEntity.ok(ressources);
    }
// //liste de tous les idsprojets
@GetMapping("/projects/ids")
public List<Integer> getAllProjectIds() {
    return projectRepository.findAllProjectIds();
}
    ///
    @Operation(summary = "supprimer ressource d'un projet ")
    @DeleteMapping("/deleteRessource/{id}")
    public ResponseEntity<?> deleteRessourceFromProject(@PathVariable Long id) {
        boolean deleted = projectService.removeRessourceFromProject(id);
        if (deleted) {
            return ResponseEntity.ok().body("✅ Ressource supprimée du projet.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Ressource non trouvée dans un projet.");
        }
    }
    @Operation(summary = "Récupérer un projet par ID")
    @GetMapping("/getProjectby/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable int id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    //
@GetMapping("/{projectId}")
public ResponseEntity<Boolean> doesProjectExist(@PathVariable int projectId) {
    boolean exists = projectRepository.existsById(projectId);
    return ResponseEntity.ok(exists);
}


//calculer le cout d'un projet sans le sauvgarder
@GetMapping("/cost/{projectId}")
public Map<String, Double> getProjectCosts(@PathVariable("projectId") int projectId) {
    logger.info("Calcul du coût pour le projet ID: {}", projectId);

    Project project = projectRepository.findById(projectId).orElse(null);
    if (project == null) {
        throw new RuntimeException("Projet non trouvé !");
    }

    logger.info("Project trouvé : {}", project.getName());
    logger.info("Equip IDs associés : {}", project.getEquipIds());
    logger.info("Ressource IDs associés : {}", project.getResourceIds());

    double totalEquipeCost = project.getEquipIds().stream()
            .flatMap(equipeId -> {
                logger.info("Appel à EquipeClient avec equipeId: {}", equipeId);
                return equipeClient.getEquipesByProject(equipeId).stream();
            })
            .mapToDouble(equipe -> ((Number) equipe.get("cost")).doubleValue())
            .sum();

    double totalRessourceCost = project.getResourceIds().stream()
            .flatMap(resourceId -> {
                logger.info("Appel à RessourceClient avec resourceId: {}", resourceId);
                return ressourceClient.getRessourcesByProject(resourceId).stream();
            })
            .mapToDouble(ressource -> ((Number) ressource.get("cost")).doubleValue())
            .sum();

    Map<String, Double> costs = new HashMap<>();
    costs.put("totalCost", totalEquipeCost + totalRessourceCost);
    logger.info("Coût total calculé: {}", costs);
    return costs;
}

}
