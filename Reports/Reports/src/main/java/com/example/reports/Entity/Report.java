package com.example.reports.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Report<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_report")
    private Long idReport;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    private LocalDateTime generatedDate;
    private LocalDateTime lastUpdated;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private String filePath;

    @Column(columnDefinition = "TEXT")
    private String jsonData;

    @ElementCollection
    @CollectionTable(name = "report_tags", joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;

    public Report(Report other) {
        this.idReport = other.idReport;
        this.title = other.title;
        this.description = other.description;
        this.reportType = other.reportType;
        this.status = other.status;
        this.generatedDate = other.generatedDate;
        this.lastUpdated = other.lastUpdated;
        this.tags = new ArrayList<>(other.tags);
    }
}