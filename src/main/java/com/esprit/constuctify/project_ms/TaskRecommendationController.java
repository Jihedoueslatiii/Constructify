package com.esprit.constuctify.project_ms;

import com.esprit.constuctify.project_ms.TaskRecommendationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class TaskRecommendationController {

    @Autowired
    private TaskRecommendationService taskRecommendationService;

    @PostMapping("/recommend-tasks-by-project")
    public ResponseEntity<String> recommendTasksByProject(@RequestBody String projectName) {
        String response = taskRecommendationService.recommendTasksByProject(projectName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recommend-tasks")
    public ResponseEntity<String> recommendTasks(@RequestBody TaskInput taskInput) {
        String response = taskRecommendationService.recommendTasks(
                taskInput.getProjectType(), taskInput.getTaskStatus(), taskInput.getPriority());
        return ResponseEntity.ok(response);
    }

    // Inner class to represent task input
    public static class TaskInput {
        private String projectType;
        private String taskStatus;
        private String priority;

        // Getters and setters
        public String getProjectType() { return projectType; }
        public void setProjectType(String projectType) { this.projectType = projectType; }
        public String getTaskStatus() { return taskStatus; }
        public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; }
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
    }
}