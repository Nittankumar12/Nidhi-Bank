package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.LoanType;
import lombok.Data;

@Data
public class SchLoanCalcDto {
    private final LoanType loanType = LoanType.Scheme;
    private double interestRate = LoanType.Scheme.getLoanInterestRate();
    private int rePaymentTerm;
    private double principalLoanAmount;
    private double payableLoanAmount;
    private double monthlyEMI;
}