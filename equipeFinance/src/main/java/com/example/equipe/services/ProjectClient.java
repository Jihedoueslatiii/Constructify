package com.example.equipe.services;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
@FeignClient(name = "PROJECT") // Nom du service Project
public interface ProjectClient {

    @PostMapping("/projects/{projectId}/assignEquipe/{equipeId}")
    String assignEquipeToProject(@PathVariable("projectId") int projectId,
                                  @PathVariable("equipeId") int equipeId);
/*
    @GetMapping("/projects/equipe/{equipeId}")
    List<Project> getProjectsByEquipe(@PathVariable("equipeId") int equipeId);
    */
}
