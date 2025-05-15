package com.example.projecttask.services;

import com.example.projecttask.entities.Project;

import java.util.List;

public interface IProjectService {
    public List<Project> getAllProjets() ;
    Project getProjectById(int id);
    public boolean removeRessourceFromProject(Long idRessource);
    public String assignRessourceToProject(int projectId, Long ressourceId);
    public int calculateProjectCost(int projectId);
    List<Integer> getAllProjectIds();
}
