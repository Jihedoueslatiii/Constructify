package com.example.projecttask.repositories;

import com.example.projecttask.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Integer> {
    @Query("SELECT p.ProjectId FROM Project p")
    List<Integer> findAllProjectIds();
}
