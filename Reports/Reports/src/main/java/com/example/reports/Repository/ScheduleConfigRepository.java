package com.example.reports.Repository;

import com.example.reports.Entity.ScheduleConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleConfigRepository extends JpaRepository<ScheduleConfig, Long> {
    // Add this method to find active schedules
    List<ScheduleConfig> findByActive(boolean active);

    // Keep your existing methods
    List<ScheduleConfig> findByReport_IdReport(Long reportId);
}