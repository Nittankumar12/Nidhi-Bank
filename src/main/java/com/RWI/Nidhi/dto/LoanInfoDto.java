package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.LoanType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanInfoDto {
    private String userEmail;
    private LocalDate startDate;
    private int rePaymentTerm;
    private double principalLoanAmount;
    private LoanType loanType;
    private double payableLoanAmount;// Total Amount user has to pay - PrincipalLoanAmount + Total EMI for repayTerm
    private double interestRate;
    private double monthlyEMI;
    private double fine;
    private LoanStatus status;
    private double discount;//only for otherLoan
    private String signUrl;
    private String signVideoUrl;

}