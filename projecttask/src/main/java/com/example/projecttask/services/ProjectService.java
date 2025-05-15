package com.example.projecttask.services;

import com.example.projecttask.entities.Project;
import com.example.projecttask.repositories.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProjectService implements IProjectService {
    ProjectRepository projectRepository;
    @Override
    public List<Project> getAllProjets() {
        return projectRepository.findAll();
    }
    @Override
    public Project getProjectById(int id) {
        return projectRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Project not found with id: " + id)
        );
    }
    //liste de tous les idsprojets
    @Override
    public List<Integer> getAllProjectIds() {
        return projectRepository.findAllProjectIds();
    }
    @Override
    public boolean removeRessourceFromProject(Long idRessource) {
        List<Project> projets = projectRepository.findAll(); // Récupère tous les projets
        boolean deleted = false;

        for (Project projet : projets) {
            if (projet.getResourceIds().contains(idRessource)) {
                projet.getResourceIds().remove(idRessource); // Supprime l'ID de la ressource
                projectRepository.save(projet);
                deleted = true;
                System.out.println("🗑️ Ressource " + idRessource + " supprimée du projet " + projet.getProjectId());
            }
        }
        return deleted;
    }
  /*  @Override
    public void assignRessource(int projectId, Long ressourceId) {
        // Vérifier que le projet existe
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec id: " + projectId));

        // Logique d'assignation de la ressource
        // Par exemple, vous pouvez mettre à jour une liste de ressources assignées au projet
        // Ici, on se contente d'un log pour illustrer l'opération
        System.out.println("Assignation de la ressource " + ressourceId + " au projet " + project.getProjectId());

        // Sauvegarder le projet si des modifications ont été apportées
        projectRepository.save(project);
    }*/
    public String assignRessourceToProject(int projectId, Long ressourceId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec id: " + projectId));

        // Ajouter la ressource à la liste s'il n'existe pas déjà
        if (!project.getResourceIds().contains(ressourceId.intValue())) {
            project.getResourceIds().add(ressourceId.longValue());
        }

        projectRepository.save(project);
        return "Ressource " + ressourceId + " affectée au projet " + projectId;
    }

    private final RessourceClient ressourceClient; // Injection du client Feign
    private final EquipeClient equipeClient; // Injection du client Feign

    public int calculateProjectCost(int projectId) {
        // Récupérer le projet à partir de son ID
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec id: " + projectId));

        // Initialiser le coût total
        int totalCost = 0;

        // Récupérer la liste des ressources assignées au projet
        List<Map<String, Object>> ressources = ressourceClient.getRessourcesByProject((long) projectId);

        // Ajouter le coût des ressources au coût total
        for (Map<String, Object> ressource : ressources) {
            int cost = (int) ressource.get("cost");
            totalCost += cost;
        }

        // Récupérer la liste des équipes assignées au projet
        List<Map<String, Object>> equipes = equipeClient.getEquipesByProject(projectId);

        // Ajouter le coût des équipes au coût total
        for (Map<String, Object> equipe : equipes) {
            int cost = (int) equipe.get("cost");
            totalCost += cost;
        }

        return totalCost;
    }


}
