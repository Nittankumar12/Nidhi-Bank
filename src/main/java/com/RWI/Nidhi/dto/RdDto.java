package com.RWI.Nidhi.dto;


import com.RWI.Nidhi.enums.RdCompoundingFrequency;
import lombok.Getter;

@Getter
public class RdDto {
    private String nomineeName;
    double monthlyDepositAmount;
    int tenure;
    RdCompoundingFrequency rdCompoundingFrequency;

}
