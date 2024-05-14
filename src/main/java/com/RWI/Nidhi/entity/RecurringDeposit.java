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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int rdId;
    private String nomineeName;
    private double monthlyDepositAmount;
    private double interestRate;
    private LocalDate startDate;
    private int tenure; // Number of months
    private double maturityAmount;
    private LocalDate maturityDate;
    //    private LocalDate lastDepositedDate;
    private double totalAmountDeposited;
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

    @OneToMany(mappedBy = "rd", cascade = CascadeType.ALL)
    private Commission commission;
}
