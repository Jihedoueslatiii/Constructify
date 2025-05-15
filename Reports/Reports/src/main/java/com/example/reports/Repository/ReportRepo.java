package com.example.reports.Repository;

import com.example.reports.Entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepo extends JpaRepository<Report, Long> {
}
