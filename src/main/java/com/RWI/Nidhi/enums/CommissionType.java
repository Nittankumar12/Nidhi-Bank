package com.RWI.Nidhi.enums;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum CommissionType {

    FD(2),
    RD(2),
    MIS(2),
    Scheme(2),
    GoldLoan(2),
    VehicleLoan(2),
    HomeLoan(2),
    BusinessLoan(2),
    PropertyLoan(2),
    AppliancesLoan(2),
    AgricultureLoan(2),
    PersonalLoan(2),
    ProductLoan(2),
    SchemeLoan(2),
    AccountOpen;

    private double commissionRate;

    CommissionType (double commissionRate){
        this.commissionRate = commissionRate;
    }
}
