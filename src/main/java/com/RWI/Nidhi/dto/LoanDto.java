package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.LoanType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanDto {
    private String email;
    private int rePaymentTerm;
    private int principalLoanAmount;
    private LoanType loanType;
    private LocalDate startDate;
    private int PayableLoanAmount;// Total Amount user has to pay - PrincipalLoanAmount + Total EMI for repayTerm
    private double interestRate;
    private int EMI;
    private int fine;
    private LoanStatus status;
}
