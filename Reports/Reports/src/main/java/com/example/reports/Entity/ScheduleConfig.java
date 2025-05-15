package com.example.reports.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ScheduleConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id", referencedColumnName = "id_report")
    private Report report;


    private String recipients;
    private String cronExpression;
    private LocalDateTime firstRun;
    private LocalDateTime lastRun;
    private boolean active = true;
    private String timeZone;

    // New fields for natural language scheduling
    private String frequency; // DAILY, WEEKLY, MONTHLY
    private LocalTime scheduledTime;
    private LocalDateTime nextRunTime;
}