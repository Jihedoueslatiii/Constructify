package com.example.proj1.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Finance {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int financeId;

    private double cost;
    private double budget;
    private double otherExpenses;
    private String description;
    private double ROI;
    // Relation One-to-One avec Projet (stocke seulement l'ID du projet)
    private Integer projectId;

}
