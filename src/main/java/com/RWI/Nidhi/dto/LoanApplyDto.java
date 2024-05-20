package com.RWI.Nidhi.dto;


import com.RWI.Nidhi.enums.LoanType;
import lombok.Data;


@Data
public class LoanApplyDto {
    private String userEmail;
    private String agentEmail;
    private int rePaymentTerm;
    private double principalLoanAmount;
    private LoanType loanType;
}