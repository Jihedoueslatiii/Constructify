package com.esprit.constuctify.project_ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService implements ITaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Task> retrieveAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task retrieveTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @Override
    public void removeTask(Long id) {
        Task task = retrieveTask(id);
        taskRepository.delete(task);
    }
    @Transactional
    public void assignTaskToProject(Long projectId, Long taskId) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (projectOptional.isPresent() && taskOptional.isPresent()) {
            Project project = projectOptional.get();
            Task task = taskOptional.get();

            // Ensure the task is assigned to the correct project
            task.setProject(project);
            taskRepository.save(task); // Save the task with the updated project reference
        } else {
            throw new IllegalArgumentException("Project or task not found.");
        }
    }




}
