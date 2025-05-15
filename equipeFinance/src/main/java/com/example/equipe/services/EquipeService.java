package com.example.equipe.services;

import com.example.equipe.entities.Equipe;
import com.example.equipe.repositories.EquipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class EquipeService implements IEquipeService {

    private final EquipeRepository equipeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Equipe> findEquipesByProjectId(int projectId) {
        List<Equipe> equipes = equipeRepository.findByIdProjet(projectId);

        if (equipes.isEmpty()) {
            throw new RuntimeException("Aucune équipe trouvée pour le projet ID: " + projectId);
        }

        return equipes;
    }
}
