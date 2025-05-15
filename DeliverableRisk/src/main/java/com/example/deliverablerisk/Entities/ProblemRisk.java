package com.example.deliverablerisk.Entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemRisk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProblemRisk;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private ProblemType type;



    @Enumerated(EnumType.STRING)
    private Probability probability; // LOW, MEDIUM, HIGH

    @Enumerated(EnumType.STRING)
    private ProblemStatus problemStatus;

    private LocalDateTime detectionDate;
    private LocalDateTime resolutionDate;

    private String appliedSolutions;



}
