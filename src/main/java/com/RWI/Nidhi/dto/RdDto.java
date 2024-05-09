package com.RWI.Nidhi.dto;


import com.RWI.Nidhi.enums.RdCompoundingFrequency;
import lombok.Data;


@Data
public class RdDto {
    private String userName;
    private String nomineeName;
    double monthlyDepositAmount;
    int tenure;
    RdCompoundingFrequency rdCompoundingFrequency;
    private String agentName;

}
