package com.esprit.pi.constructify.repositories;

import com.esprit.pi.constructify.entities.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUsername(String username);
}
