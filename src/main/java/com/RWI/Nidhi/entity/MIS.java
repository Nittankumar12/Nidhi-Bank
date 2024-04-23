package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.FdStatus;
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
public class MIS {
    @Id
    private int misId;
    private double totalDepositedAmount;
    private Date startDate;
    private Date maturityDate;
    private double monthlyIncome;
    private int tenure;
    private double interestRate;
    @Enumerated(EnumType.STRING)
    private FdStatus fdStatus;
    @ManyToOne
    @JoinColumn
    private Accounts account;
    @ManyToOne
    @JoinColumn
    private Agent agent;
    @OneToMany(mappedBy = "mis", cascade = CascadeType.ALL)
    private List<Transactions> transactionsList;
}
