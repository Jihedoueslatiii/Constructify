package com.example.reports.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContract;

    private String contractName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal contractValue;
    private LocalDate lastUpdated;
    private String notes;

    private boolean autoRenewal;
    private BigDecimal penaltyFee;
    private String currency;
    private LocalDate renewalDate;
    private boolean archived;

    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @ElementCollection
    private List<String> contractDocuments;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    @JsonIgnore
    private Supplier supplier;




}
