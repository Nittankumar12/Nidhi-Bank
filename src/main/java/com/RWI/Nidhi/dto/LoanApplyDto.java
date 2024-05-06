package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.LoanType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanApplyDto {
    private String email;
    private int rePaymentTerm;
    private double principalLoanAmount;
    private LoanType loanType;
}