package com.RWI.Nidhi.enums;

import lombok.Getter;

@Getter
public enum MisTenure {
    TWO_YEARS(2, 7.5),
    FIVE_YEARS(5,7.8),
    TEN_YEARS(10,8.0);

    private int tenure;
    private double interestRate;
    MisTenure(int tenure, double interestRate){
        this.tenure = tenure;
        this.interestRate = interestRate;
    }
}
