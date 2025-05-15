package com.example.deliverablerisk.dto;

public class ProblemRiskStatsDTO {
    private String label;
    private Long count;

    // Constructeur prenant un ENUM en premier argument et le convertissant en String
    public ProblemRiskStatsDTO(Enum<?> label, Long count) {
        this.label = label.name(); // Convertir l'ENUM en String
        this.count = count;
    }

    // Getters et setters
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
