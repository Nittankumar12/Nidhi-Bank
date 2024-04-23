package com.RWI.Nidhi.enums;

import lombok.Getter;

@Getter
public enum FdCompoundingFrequency {
    yearly(12.0, 12),
    quarterly(12.0, 12);

    private double fdInterestRate;
    private int compoundingFreq;

    FdCompoundingFrequency(double fdInterestRate, int compoundingFreq){
        this.fdInterestRate = fdInterestRate;
        this.compoundingFreq = compoundingFreq;
    }
}
