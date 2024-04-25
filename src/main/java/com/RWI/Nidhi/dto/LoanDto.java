package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.LoanType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanDto {
    private int rePaymentTerm;
    private int PrincipalLoanAmount;
    private LoanType loanType;
    private LocalDate startDate;
}
