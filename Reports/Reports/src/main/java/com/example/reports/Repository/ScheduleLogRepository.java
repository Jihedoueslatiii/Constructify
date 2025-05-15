package com.example.reports.Repository;

import com.example.reports.Entity.ScheduleLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleLogRepository extends JpaRepository<ScheduleLog, Long> {
    List<ScheduleLog> findByScheduleConfigIdOrderByRunTimeDesc(Long scheduleConfigId);
}
