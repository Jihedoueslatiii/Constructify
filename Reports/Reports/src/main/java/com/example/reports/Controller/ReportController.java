package com.example.reports.Controller;

import com.example.reports.Entity.Report;
import com.example.reports.Entity.ReportTranslation;
import com.example.reports.Service.PdfService;
import com.example.reports.Service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private PdfService pdfService;

    // Existing endpoints
    @GetMapping("/all")
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Report report = reportService.getReportById(id);
        return report != null ? ResponseEntity.ok(report) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Report> updateReport(@PathVariable Long id, @RequestBody Report reportDetails) {
        Report updatedReport = reportService.updateReport(id, reportDetails);
        return updatedReport != null ? ResponseEntity.ok(updatedReport) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<Void> archiveReport(@PathVariable Long id) {
        return reportService.archiveReport(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        return reportService.deleteReport(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    // Translation endpoints
    @GetMapping("/{id}/translations/{lang}")
    public ResponseEntity<?> getTranslation(@PathVariable Long id, @PathVariable String lang) {
        try {
            ReportTranslation translation = reportService.getTranslation(id, lang);
            if (translation == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Translation not found"));
            }
            return ResponseEntity.ok(translation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/translations")
    public ResponseEntity<?> addTranslation(
            @PathVariable Long id,
            @RequestParam String languageCode,
            @RequestParam String title,
            @RequestParam String description) {
        try {
            ReportTranslation translation = reportService.addTranslation(id, languageCode, title, description);
            return ResponseEntity.ok(translation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // New endpoints for translated reports view
    @GetMapping("/translated/{languageCode}")
    public ResponseEntity<List<Report>> getTranslatedReports(@PathVariable String languageCode) {
        List<Report> reports = reportService.getTranslatedReports(languageCode);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}/translated/{languageCode}")
    public ResponseEntity<Report> getReportTranslated(
            @PathVariable Long id,
            @PathVariable String languageCode) {
        Report report = reportService.getReportTranslated(id, languageCode);
        return report != null ? ResponseEntity.ok(report) : ResponseEntity.notFound().build();
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadPdf() {
        byte[] pdfBytes = pdfService.generatePdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reports.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @PatchMapping("/{id}/date")
    @Operation(summary = "Update report date", description = "Updates the generated date of a report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Date updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date format"),
            @ApiResponse(responseCode = "404", description = "Report not found")
    })
    public ResponseEntity<?> updateReportDate(
            @PathVariable Long id,
            @RequestBody Map<String, String> dateUpdate) {

        try {
            // Validate input
            if (!dateUpdate.containsKey("generatedDate")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "generatedDate field is required"));
            }

            // Parse and validate date
            String dateString = dateUpdate.get("generatedDate");
            LocalDateTime newDate;

            try {
                newDate = LocalDateTime.parse(dateString);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid date format. Use ISO-8601 format"));
            }

            // Update report
            boolean updated = reportService.updateReportDate(id, newDate);

            if (updated) {
                return ResponseEntity.ok()
                        .body(Map.of("message", "Report date updated successfully"));
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }


}