package com.RWI.Nidhi.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum LoanType {
    Gold(10),
    Vehicle(5),
    Appliance(7),
    Home(4),
    Scheme(10);
    private double loanInterestRate;
    LoanType(double loanInterestRate){this.loanInterestRate=loanInterestRate;}
}
