package com.esprit.constuctify.project_ms;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {

    @Autowired
    private ProjectService projectService;


    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projets = projectService.retrieveAllProjects();
        return new ResponseEntity<>(projets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project projet = projectService.retrieveProject(id);
        return new ResponseEntity<>(projet, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project projet) {
        Project createdProjet = projectService.addProject(projet);
        return new ResponseEntity<>(createdProjet, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Project> updateProject(@RequestBody Project projet) {
        Project updatedProjet = projectService.updateProject(projet);
        return new ResponseEntity<>(updatedProjet, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.removeProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}