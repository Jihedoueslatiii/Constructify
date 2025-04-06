package com.esprit.pi.constructify.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String action;
    private LocalDateTime timestamp;


}

