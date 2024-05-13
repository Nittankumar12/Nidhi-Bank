package com.RWI.Nidhi.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum LoanType {
    Gold(10),
    Vehicle(9),
    Home(8),
    BuisnessLoan(7),
    PropertyLoan(6),
    Appliances(5),
    AgricultureLoan(4),
    Personal(3),
    Scheme(100);
    private double loanInterestRate;
    LoanType(double loanInterestRate){this.loanInterestRate=loanInterestRate;}
}
