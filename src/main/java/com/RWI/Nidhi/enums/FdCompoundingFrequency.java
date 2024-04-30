package com.RWI.Nidhi.enums;

import lombok.Getter;

@Getter
public enum FdCompoundingFrequency {
    yearly(12.0, 1),// Payable yearly compounding yearly
    quarterly(12.0, 4);// Payable yearly compounding quarterly
    // Payable maturity compounding yearly

    private double fdInterestRate;
    private int compoundingFreq;

    FdCompoundingFrequency(double fdInterestRate, int compoundingFreq){
        this.fdInterestRate = fdInterestRate;
        this.compoundingFreq = compoundingFreq;
    }
}
