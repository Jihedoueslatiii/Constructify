package com.example.projecttask.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

//@FeignClient(name = "EQUIPE", path = "/equipe/api/equipe") // Service "EQUIPE" dans Eureka
@FeignClient(name = "equipe", url = "http://localhost:8083/equipe/api/equipe")

public interface EquipeClient {

    @GetMapping("/getByProjet/{projectId}")
    List<Map<String, Object>> getEquipesByProject(@PathVariable("projectId") int projectId);
}
