package com.example.projecttask.services;


        import org.springframework.cloud.openfeign.FeignClient;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PathVariable;

        import java.util.List;
        import java.util.Map;

@FeignClient(name = "RESSOURCE") // Nom du microservice Ressource dans Eureka
public interface RessourceClient {

    @GetMapping("/api/ressources/getByProjet/{projectId}")
    List<Map<String, Object>> getRessourcesByProject(@PathVariable("projectId") Long projectId);
}
