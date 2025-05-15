package com.example.reports.Service;

import com.example.reports.Entity.Report;
import com.example.reports.Entity.ReportStatus;
import com.example.reports.Entity.ReportTranslation;
import com.example.reports.Repository.ReportRepo;
import com.example.reports.Repository.ReportTranslationRepo;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepo reportRepository;

    @Autowired
    private ReportTranslationRepo reportTranslationRepo;

    // Existing methods
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(Long id) {
        return reportRepository.findById(id).orElse(null);
    }

    public Report updateReport(Long id, Report reportDetails) {
        return reportRepository.findById(id).map(report -> {
            report.setTitle(reportDetails.getTitle());
            report.setDescription(reportDetails.getDescription());
            report.setLastUpdated(LocalDateTime.now());
            return reportRepository.save(report);
        }).orElse(null);
    }

    public boolean archiveReport(Long id) {
        return reportRepository.findById(id).map(report -> {
            report.setStatus(ReportStatus.ARCHIVED);
            reportRepository.save(report);
            return true;
        }).orElse(false);
    }

    public boolean deleteReport(Long id) {
        if (reportRepository.existsById(id)) {
            reportRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Translation methods
    public ReportTranslation addTranslation(Long reportId, String languageCode,
                                            String title, String description) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        ReportTranslation translation = new ReportTranslation();
        translation.setReport(report);
        translation.setLanguageCode(languageCode);
        translation.setTitle(title);
        translation.setDescription(description);

        return reportTranslationRepo.save(translation);
    }

    public ReportTranslation getTranslation(Long reportId, String languageCode) {
        return reportTranslationRepo
                .findByReport_IdReportAndLanguageCode(reportId, languageCode)
                .orElse(null);
    }

    public List<Report> getTranslatedReports(String languageCode) {
        return reportRepository.findAll().stream()
                .map(report -> {
                    Report translated = getTranslatedVersion(report, languageCode);
                    // Ensure we never return null
                    return translated != null ? translated : report;
                })
                .collect(Collectors.toList());
    }

    public Report getReportTranslated(Long id, String languageCode) {
        return reportRepository.findById(id)
                .map(report -> getTranslatedVersion(report, languageCode))
                .orElse(null);
    }

    private Report getTranslatedVersion(Report report, String languageCode) {
        // Always return at least the English version
        if ("en".equals(languageCode)) {
            return report;
        }

        ReportTranslation translation = reportTranslationRepo
                .findByReport_IdReportAndLanguageCode(report.getIdReport(), languageCode)
                .orElse(null);

        // Create a copy of the original report
        Report translatedReport = new Report();
        translatedReport.setIdReport(report.getIdReport());
        translatedReport.setReportType(report.getReportType());
        translatedReport.setStatus(report.getStatus());
        translatedReport.setGeneratedDate(report.getGeneratedDate());
        translatedReport.setLastUpdated(report.getLastUpdated());

        // Use translated fields if available, otherwise fall back to English
        translatedReport.setTitle(translation != null ? translation.getTitle() : report.getTitle());
        translatedReport.setDescription(translation != null ? translation.getDescription() : report.getDescription());

        return translatedReport;
    }

    public byte[] generateReportData(Report report) {
        try {
            // Generate PDF data (example implementation)
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph(report.getTitle()));
            document.add(new Paragraph(report.getDescription()));
            document.close();

            byte[] pdfData = baos.toByteArray();

            // Save the report with its PDF data
            report.setFileData(pdfData);
            report.setLastUpdated(LocalDateTime.now());
            reportRepository.save(report);

            return pdfData;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate report", e);
        }
    }

    public boolean updateReportDate(Long id, LocalDateTime newDate) {
        return reportRepository.findById(id).map(report -> {
            report.setGeneratedDate(newDate);
            reportRepository.save(report);
            return true;
        }).orElse(false);
    }


}
