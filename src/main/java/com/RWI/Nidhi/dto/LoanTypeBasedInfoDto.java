package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.LoanType;
import lombok.Data;

@Data
public class LoanTypeBasedInfoDto {
    private LoanType loanType;
    private int rePaymentTerm;
    private double principalAmount;
    private double interestRate;
    private double totalPayableAmount;
    private double monthlyEMI;
}
