package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecurringDeposit {
    @Id
    private int rdId;
    private double monthlyDepositAmount;
    private double interestRate;
    private double maturityAmount;
    private int tenure; // Number of months
    private LocalDate startDate;
    private LocalDate maturityDate;
    private LocalDate lastDepositedDate;
    private LocalDate totalAmountDeposited;
    private int penalty;
    private int compoundingFrequency;
    @Enumerated(EnumType.STRING)
    private Status rdStatus;

    @ManyToOne
    @JoinColumn
    private Accounts account;
    @ManyToOne
    @JoinColumn
    private Agent agent;
    @OneToMany(mappedBy = "rd", cascade = CascadeType.ALL)
    private List<Transactions> transactionsList;
}
