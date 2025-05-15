package com.example.reports.Repository;

import com.example.reports.Entity.ReportTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportTranslationRepo extends JpaRepository<ReportTranslation, Long> {

    Optional<ReportTranslation> findByReport_IdReportAndLanguageCode(Long reportId, String languageCode);

    List<ReportTranslation> findByReport_IdReport(Long reportId);
}


