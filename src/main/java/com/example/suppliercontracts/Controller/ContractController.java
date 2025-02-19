package com.example.suppliercontracts.Controller;

import com.example.suppliercontracts.Entity.Contract;
import com.example.suppliercontracts.Service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/SupplierContracts/contracts")
@CrossOrigin(origins = "http://localhost:4200")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @PostMapping
    @Operation(summary = "Add a new contract", description = "Creates and returns a new contract")
    public ResponseEntity<Contract> addContract(@RequestBody Contract contract) {
        Contract savedContract = contractService.addContract(contract);
        return new ResponseEntity<>(savedContract, HttpStatus.CREATED);
    }

    @GetMapping("/all")
        @Operation(summary = "Get all contracts", description = "Returns a list of all contracts")
    public ResponseEntity<List<Contract>> getAllContracts() {
        List<Contract> contracts = contractService.getAllContracts();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a contract by ID", description = "Returns a contract based on the ID")
    public ResponseEntity<Contract> getContractById(@PathVariable Long id) {
        Optional<Contract> contract = contractService.getContractById(id);
        return contract.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a contract", description = "Updates and returns the updated contract")
    public ResponseEntity<Contract> updateContract(@PathVariable Long id, @RequestBody Contract contract) {
        Contract updatedContract = contractService.updateContract(id, contract);
        return updatedContract != null ?
                new ResponseEntity<>(updatedContract, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a contract", description = "Deletes a contract by ID and returns a confirmation message")
    public ResponseEntity<String> deleteContract(@PathVariable Long id) {
        String response = contractService.deleteContract(id);
        return response.equals("Contract deleted successfully.") ?
                new ResponseEntity<>(response, HttpStatus.OK) :
                new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{contractId}/assign/{supplierId}")
    @Operation(summary = "Assign a contract to a supplier", description = "Assigns a contract to a specific supplier based on their ID")
    public ResponseEntity<Contract> assignContractToSupplier(@PathVariable Long contractId, @PathVariable Long supplierId) {
        Contract contract = contractService.assignContractToSupplier(contractId, supplierId);
        return contract != null ?
                new ResponseEntity<>(contract, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
