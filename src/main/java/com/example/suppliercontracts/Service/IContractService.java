package com.example.suppliercontracts.Service;

import com.example.suppliercontracts.Entity.Contract;
import com.example.suppliercontracts.Entity.Supplier;

import java.util.List;
import java.util.Optional;

public interface IContractService {

    Contract addContract(Contract contract);

    List<Contract> getAllContracts();

    Optional<Contract> getContractById(Long id);

    Contract updateContract(Long id, Contract updatedContract);

    String deleteContract(Long id);

    Contract assignContractToSupplier(Long contractId, Long supplierId);
}
