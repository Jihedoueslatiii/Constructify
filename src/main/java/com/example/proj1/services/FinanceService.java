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
    private final EquipeClient equipeClient;
    private final RessourceClient ressourceClient;


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
        // Récupération des coûts des équipes
        List<Map<String, Object>> equipes = equipeClient.getEquipesByProject(projectId);
        double totalEquipeCost = equipes.stream()
                .mapToDouble(equipe -> ((Number) equipe.get("cost")).doubleValue())
                .sum();

        // Récupération des coûts des ressources
        List<Map<String, Object>> ressources = ressourceClient.getRessourcesByProject(projectId);
        System.out.println("🔍 Réponse API Ressource : " + ressources); // Debug pour voir le format exact

// Assure-toi que la structure de la réponse correspond bien
        if (ressources == null || ressources.isEmpty()) {
            System.out.println("⚠️ Aucune ressource trouvée pour ce projet !");
        } else {
            for (Map<String, Object> ressource : ressources) {
                System.out.println("✅ Ressource : " + ressource);
            }
        }

        // Calcul du coût total en fonction du nombre de ressources affectées
        double totalRessourceCost = ressources.stream()
                .mapToDouble(ressource -> {
                    // Vérification des données reçues
                    System.out.println("📌 Ressource JSON : " + ressource);

                    // Extraction des valeurs
                    Map<String, Integer> idProjets = (Map<String, Integer>) ressource.get("idProjets");
                    int cost = ((Number) ressource.get("cost")).intValue();

                    // Nombre de ressources affectées au projet
                    int nombreAffecte = idProjets.getOrDefault(String.valueOf(projectId), 0);

                    System.out.println("💰 Calcul : " + nombreAffecte + " * " + cost);

                    return nombreAffecte * cost;
                })
                .sum();

        System.out.println("✅ Coût total des ressources pour le projet " + projectId + " : " + totalRessourceCost);



        // Calcul du coût total
        double totalCost = totalEquipeCost + totalRessourceCost;

        // Mise à jour ou création d’une entrée Finance
        Finance finance = financeRepository.findByProjectId(projectId)
                .orElse(new Finance());

        finance.setProjectId(projectId);
        finance.setCost(totalCost);

        financeRepository.save(finance);
        System.out.println("✅ Coût du projet mis à jour : " + totalCost);
    }
   // calculer le cost de tous les projet
   @Override
    public void updateAllProjectCosts() {
        List<Integer> projectIds = projectClient.getAllProjectIds();
        for (Integer projectId : projectIds) {
            updateProjectCost(projectId);
        }
    }

}
