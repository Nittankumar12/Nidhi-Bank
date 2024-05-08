package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.Status;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RdRequestDto {
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
