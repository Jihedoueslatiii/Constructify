package com.example.proj1.controllers;


import com.example.proj1.entities.Finance;
import com.example.proj1.services.IFinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Finance Controller", description = "Gestion des Finances")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/finances")
public class FinanceController {

        private IFinanceService financeService;


    @PostMapping("/add")
    public ResponseEntity<Finance> createFinance(@RequestBody Finance finance) {
        Finance savedFinance = financeService.saveFinance(finance);
        return new ResponseEntity<>(savedFinance, HttpStatus.CREATED);
    }

     @GetMapping("/getAllFinances")
    public ResponseEntity<List<Finance>> getAllFinances() {
        List<Finance> finances = financeService.getAllFinances();
        return new ResponseEntity<>(finances, HttpStatus.OK);
    }

    @GetMapping("/getFinanceById/{id}")
    public ResponseEntity<Finance> getFinanceById(@PathVariable int id) {
        Finance finance = financeService.getFinanceById(id);
        if (finance != null) {
            return new ResponseEntity<>(finance, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")    public ResponseEntity<Finance> updateFinance(@PathVariable int id, @RequestBody Finance finance) {
        Finance updatedFinance = financeService.updateFinance(id, finance);
        if (updatedFinance != null) {
            return new ResponseEntity<>(updatedFinance, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFinance(@PathVariable int id) {
        financeService.deleteFinance(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/calculate-and-update-total-roi")
    public ResponseEntity<Double> calculateAndUpdateTotalROI() {
        double totalROI = financeService.calculateAndUpdateTotalROI();
        return new ResponseEntity<>(totalROI, HttpStatus.OK);
    }
//
@PostMapping("/{financeId}/assign-project/{projectId}")
public ResponseEntity<Finance> assignFinanceToProject(@PathVariable int financeId,
                                                      @PathVariable int projectId) {
    Finance finance = financeService.assignFinanceToProject(financeId, projectId);
    return ResponseEntity.ok(finance);
}

@Operation(summary = "calcul de cost")
@PutMapping("/update-cost/{projectId}")
public void updateProjectCost(@PathVariable("projectId") int projectId) {
    financeService.updateProjectCost(projectId);
}

//
@Operation(summary = "Calcul du coût de tous les projets")
@PutMapping("/update-cost/all")
public void updateAllProjectCosts() {
    financeService.updateAllProjectCosts();
}
}