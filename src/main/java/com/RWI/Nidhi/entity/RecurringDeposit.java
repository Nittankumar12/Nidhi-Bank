package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecurringDeposit {
    @Id
    private int recurrenceId;
    private Date startDate;
    private Date maturityDate;
    private double monthlyDepositAmount;
    private double interestRate;
    private double maturityAmount;
    @Enumerated(EnumType.STRING)
    private Status status;
    private int tenure; // Number of months
    private Date lastDepositedDate;
    private Date totalAmountDeposited;
    @ManyToOne
    @JoinColumn
    private Accounts account;
    @ManyToOne
    @JoinColumn
    private Agent agent;
    @OneToMany(mappedBy = "rd", cascade = CascadeType.ALL)
    private List<Transactions> transactionsList;
}
