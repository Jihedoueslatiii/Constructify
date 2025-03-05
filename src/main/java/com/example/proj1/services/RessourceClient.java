package com.example.proj1.services;

         import org.springframework.cloud.openfeign.FeignClient;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PathVariable;

        import java.util.List;
        import java.util.Map;

@FeignClient(name = "RESSOURCE") // Service "RESSOURCE" dans Eureka
public interface RessourceClient {

    @GetMapping("/Ressource/api/ressources/getByProjet/{projectId}")
      List<Map<String, Object>> getRessourcesByProject(@PathVariable("projectId") int projectId);
}
