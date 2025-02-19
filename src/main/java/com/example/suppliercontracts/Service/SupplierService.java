package com.example.suppliercontracts.Service;

import com.example.suppliercontracts.Entity.Supplier;
import com.example.suppliercontracts.Repository.SupplierRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepo supplierRepository;
    @Transactional
    public Supplier addSupplier(Supplier supplier) {
        // Set default values for optional fields if they are null
        if (supplier.getTotalContractValue() == null) {
            supplier.setTotalContractValue(BigDecimal.ZERO);
        }
        if (supplier.getTags() == null) {
            supplier.setTags(new ArrayList<>());
        }
        if (supplier.getContracts() == null) {
            supplier.setContracts(new ArrayList<>());
        }

        supplier.setDateAdded(LocalDate.now()); // Auto-set dateAdded
        supplier.setLastUpdated(LocalDate.now()); // Auto-set lastUpdated
        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier updateSupplier(Long id, Supplier newSupplier) {
        return supplierRepository.findById(id).map(existingSupplier -> {
            existingSupplier.setName(newSupplier.getName());
            existingSupplier.setContactPerson(newSupplier.getContactPerson());
            existingSupplier.setEmail(newSupplier.getEmail());
            existingSupplier.setPhoneNumber(newSupplier.getPhoneNumber());
            existingSupplier.setAddress(newSupplier.getAddress());
            existingSupplier.setStatus(newSupplier.getStatus());
            existingSupplier.setIndustry(newSupplier.getIndustry());
            existingSupplier.setRating(newSupplier.getRating());
            existingSupplier.setTaxId(newSupplier.getTaxId());
            existingSupplier.setTotalContractValue(newSupplier.getTotalContractValue());
            existingSupplier.setPreferredSupplier(newSupplier.isPreferredSupplier());
            existingSupplier.setReliabilityScore(newSupplier.getReliabilityScore());
            existingSupplier.setBlacklisted(newSupplier.isBlacklisted());
            existingSupplier.setTags(newSupplier.getTags());
            existingSupplier.setLastUpdated(LocalDate.now()); // Auto-update lastUpdated field

            return supplierRepository.save(existingSupplier);
        }).orElse(null);
    }

    public void deleteSupplier(Long id) {
        if (supplierRepository.existsById(id)) {
            supplierRepository.deleteById(id);
        }
    }
}
