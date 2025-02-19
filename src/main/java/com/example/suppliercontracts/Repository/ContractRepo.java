package com.example.suppliercontracts.Repository;

import com.example.suppliercontracts.Entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepo extends JpaRepository<Contract, Long> {
}
