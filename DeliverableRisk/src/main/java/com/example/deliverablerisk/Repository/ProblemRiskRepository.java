package com.example.deliverablerisk.Repository;

import com.example.deliverablerisk.Entities.ProblemRisk;
import com.example.deliverablerisk.dto.ProblemRiskStatsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRiskRepository extends JpaRepository<ProblemRisk, Long> {

    // Comptage des problèmes/risques par type
    @Query("SELECT new com.example.deliverablerisk.dto.ProblemRiskStatsDTO(p.type, COUNT(p)) FROM ProblemRisk p GROUP BY p.type")
    List<ProblemRiskStatsDTO> countByType();

    // Comptage des problèmes/risques par statut
    @Query("SELECT new com.example.deliverablerisk.dto.ProblemRiskStatsDTO(p.problemStatus, COUNT(p)) FROM ProblemRisk p GROUP BY p.problemStatus")
    List<ProblemRiskStatsDTO> countByStatus();
}
