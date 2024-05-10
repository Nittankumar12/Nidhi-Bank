package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.SchemeStatus;
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
    private int tenure;// should be counted terms of emi duration
    private double totalDepositAmount;// total amount till date
    private double interestRate;
    private SchemeStatus sStatus;//after taking loan related fields here will be fixed until closing    @OneToOne
    @OneToOne
    @JoinColumn
    private Accounts account;
    @ManyToOne
    @JoinColumn
    private Agent agent;
    @OneToMany(mappedBy = "scheme", cascade = CascadeType.ALL)
    private List<Transactions> transactionsList;
}
/*
scheme -
total amount = monthly * tenure
depending on the way tenure is counted
total deposit amount
 */