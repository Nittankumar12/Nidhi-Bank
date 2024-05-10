package com.RWI.Nidhi.dto;


import com.RWI.Nidhi.enums.RdCompoundingFrequency;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RdDto {
    double monthlyDepositAmount;
    int tenure;
    RdCompoundingFrequency rdCompoundingFrequency;
    private String nomineeName;
}
