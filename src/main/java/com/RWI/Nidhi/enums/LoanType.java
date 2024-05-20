package com.RWI.Nidhi.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum LoanType {
    Gold(2),
    Vehicle(2),
    Home(2),
    BusinessLoan(2),
    PropertyLoan(2),
    Appliances(2),
    AgricultureLoan(2),
    Personal(2),
    Scheme(2);
    private double loanInterestRate;

    LoanType(double loanInterestRate) {
        this.loanInterestRate = loanInterestRate;
    }
}
