package com.RWI.Nidhi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Commission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Double commissionAmount;
    private double CommissionRate;

    @ManyToOne
    @JoinColumn
    private Agent agent;

    @ManyToOne
    @JoinColumn
    private FixedDeposit fixedDeposit;

    @ManyToOne
    @JoinColumn
    private RecurringDeposit recurringDeposit;

    @ManyToOne
    @JoinColumn
    private MIS monthlyIncomeScheme;
}