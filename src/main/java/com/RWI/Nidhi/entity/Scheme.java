package com.RWI.Nidhi.entity;

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
public class Scheme {
    @Id
    private int schemeId;
    private double monthlyDepositAmount;
    private LocalDate startDate;
    private int tenure;// should be counted in days
    private double totalDepositAmount;// total amount till date
    private double totalInterestEarn;

    @OneToOne
    @JoinColumn
    private Accounts account;

    @ManyToOne
    @JoinColumn
    private Agent agent;

    @OneToMany(mappedBy = "scheme", cascade = CascadeType.ALL)
    private List<Transactions> transactionsList;
}
