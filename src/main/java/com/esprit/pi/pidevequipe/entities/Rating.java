package com.esprit.pi.pidevequipe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // Référence à l'utilisateur (ID)

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    @JsonIgnore
    private Teams team; // Référence à l'équipe

    @Column(nullable = false)
    private int ratingValue; // La note attribuée (par exemple, de 1 à 5)

    // Getter pour exposer uniquement l'ID de l'équipe
    @JsonProperty("teamId") // This ensures the 'teamId' field is serialized correctly in the response.
    public Long getTeamId() {
        return team != null ? team.getId() : null;
    }
}
