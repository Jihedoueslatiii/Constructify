package com.example.suppliercontracts.Service;

import com.example.suppliercontracts.Entity.Contract;
import com.example.suppliercontracts.Entity.Supplier;
import com.example.suppliercontracts.Repository.ContractRepo;
import com.example.suppliercontracts.Repository.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    @Autowired
    private ContractRepo contractRepository;

    @Autowired
    private SupplierRepo supplierRepository;

    public Contract addContract(Contract contract) {
        return contractRepository.save(contract);
    }

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    public Contract updateContract(Long id, Contract updatedContract) {
        Optional<Contract> existingContract = contractRepository.findById(id);
        if (existingContract.isPresent()) {
            Contract contract = existingContract.get();
            contract.setContractName(updatedContract.getContractName());
            contract.setStartDate(updatedContract.getStartDate());
            contract.setEndDate(updatedContract.getEndDate());
            contract.setContractValue(updatedContract.getContractValue());
            contract.setLastUpdated(updatedContract.getLastUpdated());
            contract.setNotes(updatedContract.getNotes());
            contract.setAutoRenewal(updatedContract.isAutoRenewal());
            contract.setPenaltyFee(updatedContract.getPenaltyFee());
            contract.setCurrency(updatedContract.getCurrency());
            contract.setRenewalDate(updatedContract.getRenewalDate());
            contract.setArchived(updatedContract.isArchived());
            contract.setContractStatus(updatedContract.getContractStatus());
            contract.setContractType(updatedContract.getContractType());
            contract.setContractDocuments(updatedContract.getContractDocuments());

            return contractRepository.save(contract);
        } else {
            return null;
        }
    }

    public String deleteContract(Long id) {
        if (contractRepository.existsById(id)) {
            contractRepository.deleteById(id);
            return "Contract deleted successfully.";
        } else {
            return "Contract not found, deletion failed.";
        }
    }

    public Contract assignContractToSupplier(Long contractId, Long supplierId) {
        Optional<Contract> optionalContract = contractRepository.findById(contractId);
        Optional<Supplier> optionalSupplier = supplierRepository.findById(supplierId);

        if (optionalContract.isPresent() && optionalSupplier.isPresent()) {
            Contract contract = optionalContract.get();
            Supplier supplier = optionalSupplier.get();

            contract.setSupplier(supplier);
            contractRepository.save(contract);

            return contract;
        } else {
            return null;
        }
    }
}
