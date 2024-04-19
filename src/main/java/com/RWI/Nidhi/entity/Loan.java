package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.LoanType;
import com.RWI.Nidhi.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Loan {
    @Id
    private int loanId;
    private int loanAmount;
    private int rePaymentTerm;
    private double interestRate;
    private int EMI;
    private int fine;
    @Enumerated(EnumType.STRING)
    private LoanType loanType;
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
    @ManyToOne
    @JoinColumn
    private Accounts account;
    @ManyToOne
    @JoinColumn
    private Agent agent;
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<Transactions> transactionsList;
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<Penalty> penalty;
}
