package com.RWI.Nidhi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private int tenure;
    private double totalDepositAmount;
    private double totalInterestEarn;
    @ManyToOne
    @JoinColumn
    private Accounts account;
    @ManyToOne
    @JoinColumn
    private Agent agent;
    @OneToMany(mappedBy = "scheme", cascade = CascadeType.ALL)
    private List<Transactions> transactionsList;
}
