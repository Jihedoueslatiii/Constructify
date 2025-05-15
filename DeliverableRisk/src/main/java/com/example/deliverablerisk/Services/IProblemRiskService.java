package com.example.deliverablerisk.Services;

import com.example.deliverablerisk.Entities.ProblemRisk;
import com.example.deliverablerisk.dto.ProblemRiskStatsDTO;

import java.util.List;
import java.util.Map;

public interface IProblemRiskService {

    ProblemRisk addProblemRisk(ProblemRisk problemRisk);

    List<ProblemRisk> getAllProblemRisks();

    ProblemRisk getProblemRiskById(Long idProblemRisk);

    ProblemRisk updateProblemRisk(Long idProblemRisk, ProblemRisk problemRisk);

    void deleteProblemRisk(Long idProblemRisk);

    // Statistiques par type
    Map<String, Long> getCountByType();
    Map<String, Long> getCountByStatus();
}
