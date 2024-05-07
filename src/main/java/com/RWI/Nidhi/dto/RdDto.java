package com.RWI.Nidhi.dto;


import com.RWI.Nidhi.enums.RdCompoundingFrequency;
import lombok.Data;
import lombok.Getter;

@Data
public class RdDto {
    private String nomineeName;
    double monthlyDepositAmount;
    int tenure;
    RdCompoundingFrequency rdCompoundingFrequency;

}
