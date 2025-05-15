package com.example.reports.Service;

import com.example.reports.Entity.Report;

import java.util.List;
import java.util.Optional;

public interface IReportService {
    Report createReport(Report report);
    List<Report> getAllReports();
    Optional<Report> getReportById(Long id);
    Report updateReport(Long id, Report reportDetails);
}
