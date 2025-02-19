package com.example.suppliercontracts.Repository;

import com.example.suppliercontracts.Entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepo  extends JpaRepository<Supplier, Long> {
}
