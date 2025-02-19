package com.example.suppliercontracts.Controller;

import com.example.suppliercontracts.Entity.Supplier;
import com.example.suppliercontracts.Service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "http://localhost:4200")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping("/add")
    @Operation(summary = "Add a new supplier", description = "Creates and adds a new supplier to the system.")
    public ResponseEntity<Supplier> addSupplier(@RequestBody Supplier supplier) {
        Supplier newSupplier = supplierService.addSupplier(supplier);
        return ResponseEntity.ok(newSupplier);
    }

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }





    @GetMapping("/{id}")
    @Operation(summary = "Get a supplier by ID", description = "Fetches the details of a supplier by their ID.")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
        Optional<Supplier> supplier = supplierService.getSupplierById(id);
        return supplier.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a supplier", description = "Updates the details of an existing supplier based on their ID.")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier newSupplier) {
        Supplier updatedSupplier = supplierService.updateSupplier(id, newSupplier);
        return (updatedSupplier != null) ? ResponseEntity.ok(updatedSupplier) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a supplier", description = "Deletes a supplier from the system based on their ID.")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();    }


}

