package com.RWI.Nidhi.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.EnumSet;

@Getter
@NoArgsConstructor
public enum LoanType {
    Gold(2),
    Vehicle(2),
    Home(2),
    Business(2),
    Property(2),
    Appliances(2),
    Agriculture(2),
    Personal(2),
    Product(2),
    Scheme(2);

    private double loanInterestRate;
    LoanType(double loanInterestRate) {
        this.loanInterestRate = loanInterestRate;
    }
}
