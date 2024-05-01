package com.RWI.Nidhi.enums;

import lombok.Getter;

@Getter
public enum RdCompoundingFrequency {
    yearly(12.0, 12),
    quarterly(12.0, 12);

    private double rdInterestRate;
    private int compoundingFreq;

    RdCompoundingFrequency(double rdInterestRate, int compoundingFreq){
        this.rdInterestRate = rdInterestRate;
        this.compoundingFreq = compoundingFreq;
    }
}
