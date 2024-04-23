package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.FdCompoundingFrequency;
import com.RWI.Nidhi.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FixedDeposit {
    @Id
    private int fdId;
    private double amount;
    private Date depositDate;
    private Date maturityDate;
    @Transient
    @Enumerated
    private FdCompoundingFrequency fdCompoundingFrequency;
    private int compoundingFrequency;
    private double interestRate;
    private int tenure;
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
    public void setInterestRate(FdCompoundingFrequency fdCompoundingFrequency) {
        this.interestRate = fdCompoundingFrequency.getFdInterestRate();
        this.compoundingFrequency = fdCompoundingFrequency.getCompoundingFreq();
    }
}
