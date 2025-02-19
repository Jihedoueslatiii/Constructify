package com.example.suppliercontracts.Service;

import com.example.suppliercontracts.Entity.Supplier;

import java.util.List;
import java.util.Optional;

public interface ISupplierService {

    Supplier addSupplier(Supplier supplier);

    List<Supplier> getAllSuppliers();

    Optional<Supplier> getSupplierById(Long id);

    Supplier updateSupplier(Long id, Supplier newSupplier);

    String deleteSupplier(Long id);
}
