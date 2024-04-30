package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.LoanType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Loan {
    @Id
    private int loanId;
    private int PayableLoanAmount;// Total Amount user has to pay - PrincipalLoanAmount + Total EMI for repayTerm
    private int PrincipalLoanAmount;//Loan Amount user gets as Loan
    private int rePaymentTerm; //should be counted in days
    private LocalDate startDate;
    private double interestRate;
    private int EMI;
    private int fine;
    @Enumerated(EnumType.STRING)
    private LoanType loanType;
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
//    public void setInterestRate(LoanType loanType) {
//        this.interestRate = loanType.getLoanInterestRate();
//    }
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
