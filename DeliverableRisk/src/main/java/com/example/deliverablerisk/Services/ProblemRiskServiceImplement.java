package com.example.deliverablerisk.Services;
import com.example.deliverablerisk.dto.ProblemRiskStatsDTO;
import com.example.deliverablerisk.Entities.ProblemRisk;
import com.example.deliverablerisk.Repository.ProblemRiskRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProblemRiskServiceImplement implements IProblemRiskService {

    private final ProblemRiskRepository problemRiskRepository;

    public ProblemRiskServiceImplement(ProblemRiskRepository problemRiskRepository) {
        this.problemRiskRepository = problemRiskRepository;
    }

    @Override
    public ProblemRisk addProblemRisk(ProblemRisk problemRisk) {
        return problemRiskRepository.save(problemRisk);
    }

    @Override
    public List<ProblemRisk> getAllProblemRisks() {
        return problemRiskRepository.findAll();
    }

    @Override
    public ProblemRisk getProblemRiskById(Long id) {
        return problemRiskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem/Risk not found with ID: " + id));
    }

    @Override
    public ProblemRisk updateProblemRisk(Long idProblemRisk, ProblemRisk updatedProblemRisk) {
        ProblemRisk existingProblemRisk = getProblemRiskById(idProblemRisk);

        existingProblemRisk.setTitle(updatedProblemRisk.getTitle());
        existingProblemRisk.setDescription(updatedProblemRisk.getDescription());
        existingProblemRisk.setType(updatedProblemRisk.getType());
        existingProblemRisk.setProbability(updatedProblemRisk.getProbability());
        existingProblemRisk.setProblemStatus(updatedProblemRisk.getProblemStatus());
        existingProblemRisk.setDetectionDate(updatedProblemRisk.getDetectionDate());
        existingProblemRisk.setResolutionDate(updatedProblemRisk.getResolutionDate());
        existingProblemRisk.setAppliedSolutions(updatedProblemRisk.getAppliedSolutions());

        return problemRiskRepository.save(existingProblemRisk);
    }

    @Override
    public void deleteProblemRisk(Long idProblemRisk) {
        if (!problemRiskRepository.existsById(idProblemRisk)) {
            throw new RuntimeException("Problem/Risk not found with ID: " + idProblemRisk);
        }
        problemRiskRepository.deleteById(idProblemRisk);
    }

    @Override
    public Map<String, Long> getCountByType() {
        List<ProblemRiskStatsDTO> results = problemRiskRepository.countByType();
        return convertToMap(results);
    }

    @Override
    public Map<String, Long> getCountByStatus() {
        List<ProblemRiskStatsDTO> results = problemRiskRepository.countByStatus();
        return convertToMap(results);
    }

    // Méthode utilitaire pour convertir une liste de DTOs en Map<String, Long>
    private Map<String, Long> convertToMap(List<ProblemRiskStatsDTO> results) {
        Map<String, Long> map = new HashMap<>();
        for (ProblemRiskStatsDTO row : results) {
            if (row.getLabel() != null && row.getCount() != null) {
                map.put(row.getLabel(), row.getCount());
            }
        }
        return map;
    }
}
