package com.example.equipe.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int equipeId;
    private String nomEquipe;
    private String typeEquipe;
    private int cost;
    // Une équipe est assignée à  un projets
    @JsonProperty("idProjet")
    private int idProjet;


}
