package com.example.proj1.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

// Appelle le microservice Projet
@FeignClient(name = "PROJECT") // Nom du service dans Eureka
public interface ProjectClient {

    @GetMapping("/project/projects/{projectId}")
    boolean doesProjectExist(@PathVariable("projectId") int projectId);
    @GetMapping("/{projectId}")
    Map<String, Object> getProjectById(@PathVariable("projectId") int projectId);
    @GetMapping("/api/project/cost/{projectId}")
    Map<String, Double> getProjectCosts(@PathVariable("projectId") int projectId);
    @GetMapping("/project/projects/projects/ids") // Assure-toi que cette route existe dans le microservice `projet`
    List<Integer> getAllProjectIds();
}