package com.example.equipe.repositories;

import com.example.equipe.entities.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipeRepository extends JpaRepository<Equipe, Integer> {
    List<Equipe> findByIdProjet(int idProjet);
}
