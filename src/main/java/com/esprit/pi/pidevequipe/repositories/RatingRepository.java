package com.esprit.pi.pidevequipe.repositories;

import com.esprit.pi.pidevequipe.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByTeam_Id(Long teamId);
    List<Rating> findByUserId(Long userId);
}
