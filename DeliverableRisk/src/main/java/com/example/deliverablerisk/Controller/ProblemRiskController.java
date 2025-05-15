package com.example.deliverablerisk.Controller;

import com.example.deliverablerisk.Entities.ProblemRisk;
import com.example.deliverablerisk.Services.IProblemRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/problem-risks")
public class ProblemRiskController {

    @Autowired
    private IProblemRiskService problemRiskService;

    @PostMapping("/add")
    public ResponseEntity<ProblemRisk> addProblemRisk(@RequestBody ProblemRisk problemRisk) {
        System.out.println("Reçu : " + problemRisk);
        return ResponseEntity.ok(problemRiskService.addProblemRisk(problemRisk));
    }


    @GetMapping
    public List<ProblemRisk> getAllProblemRisks() {
        return problemRiskService.getAllProblemRisks();
    }

    @GetMapping("/{id}")
    public ProblemRisk getProblemRiskById(@PathVariable Long id) {
        return problemRiskService.getProblemRiskById(id);
    }

    @PutMapping("/update/{id}")
    public ProblemRisk updateProblemRisk(@PathVariable Long id, @RequestBody ProblemRisk updatedProblemRisk) {
        return problemRiskService.updateProblemRisk(id, updatedProblemRisk);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProblemRisk(@PathVariable Long id) {
        problemRiskService.deleteProblemRisk(id);
    }

    @GetMapping("/stats/by-type")
    public Map<String, Long> getCountByType() {
        return problemRiskService.getCountByType();
    }

    // /api/problem-risks/stats/by-status
    @GetMapping("/stats/by-status")
    public Map<String, Long> getCountByStatus() {
        return problemRiskService.getCountByStatus();
    }
}

