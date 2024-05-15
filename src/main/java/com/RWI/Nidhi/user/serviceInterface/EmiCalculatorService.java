package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.enums.LoanType;

public interface EmiCalculatorService {
    double[] calculateEMI(double principle,LoanType loanType, int time);
}
