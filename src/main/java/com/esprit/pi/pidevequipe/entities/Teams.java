package com.esprit.pi.pidevequipe.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Teams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column(nullable = false)
    private String teamName;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Rating> ratings = new HashSet<>();

    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream().mapToInt(Rating::getRatingValue).average().orElse(0.0);
    }
}
