package com.example.proj1.services;

import com.example.proj1.entities.Finance;

import java.util.List;

public interface IFinanceService {
    Finance saveFinance(Finance finance);
    List<Finance> getAllFinances();
    Finance getFinanceById(int id);
    Finance updateFinance(int id, Finance finance);
    void deleteFinance(int id);
    double calculateAndUpdateTotalROI(); // Nouvelle méthode
//
     Finance assignFinanceToProject(int financeId, int projectId);
       void updateProjectCost(int projectId);
    void updateAllProjectCosts();
}
