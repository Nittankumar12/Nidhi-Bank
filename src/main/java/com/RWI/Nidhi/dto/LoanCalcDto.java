package com.RWI.Nidhi.dto;

import com.RWI.Nidhi.enums.LoanType;
import lombok.*;

//@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanCalcDto {
    private LoanType loanType;
    private double interestRate = loanType.getLoanInterestRate();
    private int rePaymentTerm;
    private double principalLoanAmount;
    private double payableLoanAmount;
    private double monthlyEMI;

    public LoanCalcDto(SchLoanCalcDto schLoanCalcDto) {
        this.loanType = schLoanCalcDto.getLoanType();
        this.interestRate = schLoanCalcDto.getInterestRate();
        this.rePaymentTerm = schLoanCalcDto.getRePaymentTerm();
        this.principalLoanAmount = schLoanCalcDto.getPrincipalLoanAmount();
        this.payableLoanAmount = schLoanCalcDto.getPayableLoanAmount();
        this.monthlyEMI = schLoanCalcDto.getMonthlyEMI();
    }
}
