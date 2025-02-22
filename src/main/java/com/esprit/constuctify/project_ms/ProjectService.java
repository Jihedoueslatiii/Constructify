package com.esprit.constuctify.project_ms;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService implements IProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Override
    public List<Project> retrieveAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project addProject(Project project) {



        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project retrieveProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'id: " + id));
    }

    @Override
    public void removeProject(Long id) {
        Project project = retrieveProject(id);
        projectRepository.delete(project);
    }
    @Transactional
    public void assignTaskToProject(Long projectId, Long taskId) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (projectOptional.isPresent() && taskOptional.isPresent()) {
            Project project = projectOptional.get();
            Task task = taskOptional.get();

            // Check if the task is not already assigned to another project
            if (task.getProject() == null) {
                task.setProject(project);
                taskRepository.save(task); // This will update the task in the database
            } else {
                throw new IllegalStateException("Task is already assigned to another project.");
            }
        } else {
            throw new IllegalArgumentException("Project or task not found.");
        }
    }




}