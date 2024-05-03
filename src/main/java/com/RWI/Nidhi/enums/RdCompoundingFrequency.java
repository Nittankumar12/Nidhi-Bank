package com.RWI.Nidhi.enums;

import lombok.Getter;

@Getter
public enum RdCompoundingFrequency {
    yearly(12.0, 1), // Payable yearly compounding yearly
    quarterly(12.0, 4); // Payable yearly compounding quarterly

    private double rdInterestRate;
    private int compoundingFreq;

    RdCompoundingFrequency(double rdInterestRate, int compoundingFreq){
        this.rdInterestRate = rdInterestRate;
        this.compoundingFreq = compoundingFreq;
    }
}
