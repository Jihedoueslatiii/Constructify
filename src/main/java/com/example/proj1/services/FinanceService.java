package com.example.proj1.services;

import com.example.proj1.entities.Finance;
import com.example.proj1.repositories.FinanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FinanceService implements IFinanceService {


  //  @Autowired
    private FinanceRepository financeRepository;
    private final ProjectClient projectClient; // Feign Client pour vérifier le projet



    @Override
    public Finance saveFinance(Finance finance) {
        return financeRepository.save(finance);
    }

    @Override
    public List<Finance> getAllFinances() {
        return financeRepository.findAll();
    }

    @Override
    public Finance getFinanceById(int id) {
        Optional<Finance> finance = financeRepository.findById(id);
        return finance.orElse(null);
    }

    @Override
    public Finance updateFinance(int id, Finance finance) {
        if (financeRepository.existsById(id)) {
            finance.setFinanceId(id);
            return financeRepository.save(finance);
        }
        return null;
    }

    @Override
    public void deleteFinance(int id) {
        financeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public double calculateAndUpdateTotalROI() {
        List<Finance> finances = financeRepository.findAll();
        double totalROI = 0;

        for (Finance finance : finances) {
            double roiPercentage;

            if (finance.getBudget() < 10000) {
                roiPercentage = 0.05;  // 5%
            } else if (finance.getBudget() >= 10000 && finance.getBudget() < 50000) {
                roiPercentage = 0.10;  // 10%
            } else if (finance.getBudget() >= 50000 && finance.getBudget() < 100000) {
                roiPercentage = 0.15;  // 15%
            } else {
                roiPercentage = 0.20;  // 20%
            }

            double roi = roiPercentage * finance.getBudget();
            finance.setROI(roi); // Mise à jour du champ ROI
            totalROI += roi;
        }

        financeRepository.saveAll(finances); // Optimisation : mise à jour en un seul appel

        return totalROI;
    }


//////

    @Override
    public Finance assignFinanceToProject(int financeId, int projectId) {
        Optional<Finance> financeOpt = financeRepository.findById(financeId);
        if (!financeOpt.isPresent()) {
            throw new RuntimeException("Finance non trouvée avec id: " + financeId);
        }

        // Vérifier si le projet existe via le Feign Client
        boolean projectExists = projectClient.doesProjectExist(projectId);
        if (!projectExists) {
            throw new RuntimeException("Projet non trouvé avec id: " + projectId);
        }

        // Associer la finance au projet
        Finance finance = financeOpt.get();
        finance.setProjectId(projectId);
        return financeRepository.save(finance);
    }

//calculer le cost d'un projet

    @Transactional
    public void updateProjectCost(int projectId) {
        // 🔹 Appelle PROJET pour obtenir le coût total
        Map<String, Double> costs = projectClient.getProjectCosts(projectId);
        double totalCost = costs.get("totalCost");

        // 🔹 Met à jour la table Finance
        Finance finance = financeRepository.findByProjectId(projectId)
                .orElse(new Finance());
        finance.setProjectId(projectId);
        finance.setCost(totalCost);
        financeRepository.save(finance);

        System.out.println("✅ Coût du projet mis à jour : " + totalCost);
    }

}
