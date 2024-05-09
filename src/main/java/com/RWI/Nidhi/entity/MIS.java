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
public class MIS {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int misId;
    private double totalDepositedAmount;
    private LocalDate startDate;
    private int tenure;
    private double interestRate;
    private String nomineeName;
    private LocalDate maturityDate;
    private LocalDate closingDate;
    private double monthlyIncome;
    private double totalInterestEarned;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn
    private Accounts account;

    @ManyToOne
    @JoinColumn
    private Agent agent;

    @OneToMany(mappedBy = "mis", cascade = CascadeType.ALL)
    private List<Transactions> transactionsList;
}
