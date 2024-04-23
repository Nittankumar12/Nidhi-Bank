package com.RWI.Nidhi.enums;

import lombok.Getter;

@Getter
public enum FdCompoundingFrequency {
    yearly(12, 12),
    quarterly(12, 12);

    private int fdInterestRate;
    private int compoundingFreq;

    FdCompoundingFrequency(int fdInterestRate, int compoundingFreq){
        this.fdInterestRate = fdInterestRate;
        this.compoundingFreq = compoundingFreq;
    }
}
