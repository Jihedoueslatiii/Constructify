package com.esprit.pi.pidevequipe.repositories;

import com.esprit.pi.pidevequipe.entities.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamsRepository extends JpaRepository<Teams, Long> {
    boolean existsByTeamName(String teamName);
}
