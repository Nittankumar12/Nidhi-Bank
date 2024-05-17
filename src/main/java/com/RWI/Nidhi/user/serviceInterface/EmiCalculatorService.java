package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.enums.LoanType;

import java.util.HashMap;

public interface EmiCalculatorService {
    HashMap<String, Double> calculateEMI(double principle, LoanType loanType, int time);
}
