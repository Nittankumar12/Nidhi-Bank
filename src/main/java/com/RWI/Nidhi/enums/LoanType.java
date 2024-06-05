package com.RWI.Nidhi.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.EnumSet;

@Getter
@NoArgsConstructor
public enum LoanType {
    Gold(2),
    Vehicle(4),
    Home(2.5),
    Business(6),
    Property(5.5),
    Appliances(3),
    Agriculture(4),
    Personal(6.8),
    Other(2.2),
    Scheme(3.4);

    private double loanInterestRate;
    LoanType(double loanInterestRate) {
        this.loanInterestRate = loanInterestRate;
    }
}
