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
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTask;

    private String title;
    private String description;

    @Temporal(TemporalType.DATE)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

//    @ManyToOne
//    @JoinColumn(name = "project_id")
//    @JsonIgnore
//
@ManyToOne
@JoinColumn(name = "project_id") // Foreign key column in the Task table
@JsonIgnore // Prevents infinite recursion in JSON serialization
private Project project;

    @JsonProperty("idTask")
    public String getFormattedId() {
        return String.format("TSK_%03d", idTask);
    }

    @JsonProperty("idTask")
    public void setFormattedId(String id) {
        if (id != null && id.startsWith("TSK_")) {
            try {
                this.idTask = Long.parseLong(id.substring(4));
            } catch (NumberFormatException e) {
                // Handle invalid format
            }
        }
    }
}