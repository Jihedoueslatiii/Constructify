package com.example.deliverablerisk.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate; // Changé de Date à LocalDate
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deliverable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idDeliverable;

    @Enumerated(EnumType.STRING)
    private Status Status;

    String name;

    @Column(columnDefinition = "DATE") // Spécifie le type DATE seulement
    LocalDate expected_date;

    @Column(columnDefinition = "DATE")
    LocalDate delivery_date;
    String Responsable;
    String Priorite;
    float Cout_estime;
    String Commentaire;
    String Valide;
}