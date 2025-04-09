package com.esprit.constuctify.project_ms;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

@Service
public class TaskRecommendationService {

    private final String FLASK_API_URL = "http://localhost:5000";
    private final RestTemplate restTemplate = new RestTemplate();

    public String recommendTasksByProject(String projectName) {
        String url = FLASK_API_URL + "/recommend_tasks_by_project";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"project_name\": \"" + projectName + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    public String recommendTasks(String projectType, String taskStatus, String priority) {
        String url = FLASK_API_URL + "/recommend_tasks";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = String.format(
                "{\"project_type\": \"%s\", \"task_status\": \"%s\", \"priority\": \"%s\"}",
                projectType, taskStatus, priority);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }
}