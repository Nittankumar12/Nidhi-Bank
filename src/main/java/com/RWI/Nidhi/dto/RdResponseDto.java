package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RdResponseDto {
    private String userName;
    private double monthlyDepositAmount;
    private double interestRate;
    private LocalDate startDate;
    private int tenure; // Number of months
    private double maturityAmount;
    private String nomineeName;
    private LocalDate maturityDate;
//    private LocalDate lastDepositedDate;
    private double totalAmountDeposited;
    private int compoundingFrequency;
    private Status rdStatus;
    private String agentName;

}
