package com.example.reports.Repository;


import com.example.reports.Entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
    List<Supplier> findAllByRatingGreaterThan(String rating);
}
