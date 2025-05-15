package com.example.reports.Controller;

import com.example.reports.Entity.ScheduleConfig;
import com.example.reports.Service.ReportSchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;  // Add this import for List

// ScheduleController.java
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ReportSchedulingService schedulingService;

    @PostMapping
    public ResponseEntity<ScheduleConfig> createSchedule(
            @RequestParam Long reportId,
            @RequestBody ScheduleRequest request) {

        ScheduleConfig config = schedulingService.createSchedule(request.getCommand(), reportId);
        return ResponseEntity.ok(config);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleConfig> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(schedulingService.getScheduleById(id));  // Changed method name
    }

    @GetMapping("/report/{reportId}")
    public ResponseEntity<List<ScheduleConfig>> getSchedulesForReport(@PathVariable Long reportId) {
        return ResponseEntity.ok(schedulingService.getSchedulesByReportId(reportId));  // Changed method name
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        schedulingService.cancelSchedule(id);  // Changed method name
        return ResponseEntity.noContent().build();
    }

}