package com.esprit.pi.pidevequipe.services;

import com.esprit.pi.pidevequipe.entities.Rating;
import com.esprit.pi.pidevequipe.entities.Teams;
import com.esprit.pi.pidevequipe.repositories.RatingRepository;
import com.esprit.pi.pidevequipe.repositories.TeamsRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RatingServicesImpl implements IRatingService {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private TeamsRepository teamsRepository;

    @Override
    public void addRating(Long userId, Long teamId, int ratingValue) {
        Teams team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Rating rating = new Rating();
        rating.setUserId(userId);
        rating.setTeam(team);
        rating.setRatingValue(ratingValue);

        ratingRepository.save(rating);
    }

    // Récupérer la moyenne des notes d'une équipe
    @Override
    public double getAverageRating(Long teamId) {
        Teams team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        return team.getAverageRating();
    }
}
