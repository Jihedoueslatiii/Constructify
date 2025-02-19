package com.esprit.constuctify.project_ms;


import java.util.List;

public interface IProjectService {
    List<Project> retrieveAllProjects();
    Project addProject(Project project);
    Project updateProject(Project project);
    Project retrieveProject(Long id);
    void removeProject(Long id);
}