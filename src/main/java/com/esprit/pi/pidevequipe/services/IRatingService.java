package com.esprit.pi.pidevequipe.services;

public interface IRatingService {
    void addRating(Long userId, Long teamId, int ratingValue);

    // Récupérer la moyenne des notes d'une équipe
    double getAverageRating(Long teamId);
}
