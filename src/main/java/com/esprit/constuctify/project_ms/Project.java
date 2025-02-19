package com.esprit.constuctify.project_ms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString

@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProjet;


    private String nomProjet;
    private String descriptionProjet;

    @Temporal(TemporalType.DATE)
    private LocalDate dateDebut;

    @Temporal(TemporalType.DATE)
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private ProjectStatus etatProjet;

    @JsonProperty("idProjet")
    public String getFormattedId() {
        return String.format("PRJ_%03d", idProjet);
    }

    @JsonProperty("idProjet")
    public void setFormattedId(String id) {
        if (id != null && id.startsWith("PRJ_")) {
            try {
                this.idProjet = Long.parseLong(id.substring(4));
            } catch (NumberFormatException e) {
                // Handle invalid format
            }
        }
    }
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Prevents infinite recursion in JSON serialization
    private Set<Task> tasks;

    //@JsonIgnore
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "projet")
    //private Set<Task> tasks;

}