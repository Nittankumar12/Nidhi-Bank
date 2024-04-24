package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FixedDeposit {
    @Id
    private int fdId;
    private int amount;
    private LocalDate depositDate;
    private LocalDate maturityDate;
    private LocalDate closingDate;
    private int fineAmount;
//    @Transient
//    @Enumerated(EnumType.STRING)
//    private FdCompoundingFrequency fdCompoundingFrequency;
    private int compoundingFrequency;
    private double interestRate;
    private int tenure;
    private double maturityAmount;

    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    @JoinColumn
    private Accounts account;
    @ManyToOne
    @JoinColumn
    private Agent agent;
    @OneToMany(mappedBy = "fd", cascade = CascadeType.ALL)
    private List<Transactions> transactionsList;
//    public void setInterestRate(FdCompoundingFrequency fdCompoundingFrequency) {
//        this.interestRate = fdCompoundingFrequency.getFdInterestRate();
//        this.compoundingFrequency = fdCompoundingFrequency.getCompoundingFreq();
//    }
}
