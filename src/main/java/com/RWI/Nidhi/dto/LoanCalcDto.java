package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.LoanType;
import lombok.*;

//@Data

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalcDto {
    private LoanType loanType;
    private double interestRate;
    private int rePaymentTerm;
    private double principalLoanAmount;
    private double payableLoanAmount;
    private double monthlyEMI;

    public void setInterestRate(LoanType loanType) {
        this.interestRate = loanType.getLoanInterestRate();
    }

    public LoanCalcDto(SchLoanCalcDto schLoanCalcDto) {
        this.loanType = schLoanCalcDto.getLoanType();
        this.interestRate = schLoanCalcDto.getInterestRate();
        this.rePaymentTerm = schLoanCalcDto.getRePaymentTerm();
        this.principalLoanAmount = schLoanCalcDto.getPrincipalLoanAmount();
        this.payableLoanAmount = schLoanCalcDto.getPayableLoanAmount();
        this.monthlyEMI = schLoanCalcDto.getMonthlyEMI();
    }
}