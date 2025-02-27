package com.example.proj1.repositories;

import com.example.proj1.entities.Finance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Integer> {
    Optional<Finance> findByProjectId(int projectId);
    // Vous pouvez ajouter des méthodes de requête personnalisées ici si nécessaire
}
