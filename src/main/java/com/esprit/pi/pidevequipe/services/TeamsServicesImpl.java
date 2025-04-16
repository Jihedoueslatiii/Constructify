package com.esprit.pi.pidevequipe.services;

import com.esprit.pi.pidevequipe.entities.Teams;
import com.esprit.pi.pidevequipe.repositories.TeamsRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamsServicesImpl implements ITeamsService {
    @Autowired
    private TeamsRepository teamsRepository;

    @Autowired
    private RestTemplate restTemplate; // Pour appeler l’API du projet User

    @Override
    public void removeEmployeeFromTeam(Long teamId, Long employeeId) {
        Teams team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Appeler l'API du projet User pour supprimer l'équipe de l'utilisateur
        restTemplate.put("http://localhost:8089/Constructify/user/removeFromTeam/{userId}", null, employeeId);
        teamsRepository.save(team);
    }

    @Override
    public Teams addTeam(Teams team) {
        if (teamsRepository.existsByTeamName(team.getTeamName())) {
            throw new RuntimeException("A team with the same name already exists.");
        }
        team.setId(null);
        return teamsRepository.save(team);
    }
    public Teams addEmployeeToTeam(Long teamId, Long employeeId) {
        Teams team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        //team.getEmployeeIds().add(employeeId); // Ajouter l'ID de l'employé
        return teamsRepository.save(team);
    }

    public List<Teams> getAllTeams() {
        return teamsRepository.findAll();
    }

    @Override
    public void deleteTeam(Long teamId) {
        Teams team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Supprimer l'équipe
        teamsRepository.delete(team);
    }

    public Teams getTeamById(Long teamId) {
        return teamsRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
    }



}
