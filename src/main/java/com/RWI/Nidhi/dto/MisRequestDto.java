package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MisRequestDto {
    private String userName;
    private double totalDepositedAmount;
    private LocalDate startDate;
    private int tenure;
    private double interestRate;
    private String nomineeName;
    private LocalDate maturityDate;
    private double monthlyIncome;
    private double totalInterestEarned;
    private Status misStatus;
    private String agentName;
}
