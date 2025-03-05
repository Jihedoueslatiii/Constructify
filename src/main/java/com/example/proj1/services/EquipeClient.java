package com.example.proj1.services;

        import org.springframework.cloud.openfeign.FeignClient;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PathVariable;

        import java.util.List;
        import java.util.Map;

@FeignClient(name = "EQUIPE") // Service "EQUIPE" dans Eureka

public interface EquipeClient {

    @GetMapping("/equipe/api/equipe/getByProjet/{projectId}")
    List<Map<String, Object>> getEquipesByProject(@PathVariable("projectId") int projectId);
}
