package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.LoanType;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanClosureDto {
    private LoanStatus status;
    private LoanType loanType;
    private double principalLoanAmount;
    private int rePaymentTerm;
    private LocalDate startDate;
    private double monthlyEMI;
    private double fine;
    private LocalDate lastEMIDate;
    private String finalStatement;
}