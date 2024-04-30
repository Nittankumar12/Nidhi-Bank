package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.TransactionStatus;
import com.RWI.Nidhi.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transactions {
    @Id
    private int transactionId;
    @UpdateTimestamp
    private Date transactionDate;
    @Enumerated(EnumType.STRING)
    private TransactionType  transactionType;

//    private Time transactionTime;
//    private String transactionType; //Deposited
    private double transactionAmount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
    @ManyToOne
    @JoinColumn
    private Accounts account;
    @ManyToOne
    @JoinColumn
    private FixedDeposit fd;
    @ManyToOne
    @JoinColumn
    private Loan loan;
    @ManyToOne
    @JoinColumn
    private MIS mis;
    @ManyToOne
    @JoinColumn
    private RecurringDeposit rd;
    @ManyToOne
    @JoinColumn
    private Scheme scheme;
}
