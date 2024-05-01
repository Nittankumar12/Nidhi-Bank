package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.LoanType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanDto {
    private String email;
    private int rePaymentTerm;
    private double principalLoanAmount;
    private LoanType loanType;
    private LocalDate startDate;
    private double payableLoanAmount;// Total Amount user has to pay - PrincipalLoanAmount + Total EMI for repayTerm
    private double interestRate;
    private double monthlyEMI;
    private double fine;
    private LoanStatus status;
}
