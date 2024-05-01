package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.LoanType;
import lombok.Data;

@Data
public class LoanCalcDto {
    private LoanType loanType;
    private double interestRate;
    private int rePaymentTerm;
    private double principalLoanAmount;
    private double payableLoanAmount;
    private double monthlyEMI;
}
