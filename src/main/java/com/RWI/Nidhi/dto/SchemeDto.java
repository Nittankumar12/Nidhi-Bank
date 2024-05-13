package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.SchemeStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchemeDto {
    private int schemeId;
    private double monthlyDepositAmount;
    private int tenure;// should be counted terms of emi duration
    private double interestRate;
    private SchemeStatus sStatus;
}
