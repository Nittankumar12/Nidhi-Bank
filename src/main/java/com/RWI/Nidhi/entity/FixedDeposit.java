package com.RWI.Nidhi.entity;

import com.RWI.Nidhi.enums.Status;
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
public class FixedDeposit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int fdId;
    private String nomineeName;
    private int amount;
    private LocalDate depositDate;
    private double interestRate;
    private int tenure;
    private double maturityAmount;
    private LocalDate maturityDate;
    private LocalDate closingDate;
    private int penalty;
    private int compoundingFrequency;
//    @Transient
//    @Enumerated(EnumType.STRING)
//    private FdCompoundingFrequency fdCompoundingFrequency;

    @Enumerated(EnumType.STRING)
    private Status fdStatus;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Accounts account;

    @ManyToOne
    @JoinColumn
    private Agent agent;

    @OneToMany(mappedBy = "fd", cascade = CascadeType.ALL)
    private List<Transactions> transactionsList;

}
