package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.enums.LoanType;
import com.RWI.Nidhi.user.serviceInterface.EmiCalculatorService;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class EmiCalculatorServiceImplementation implements EmiCalculatorService {

    @Override
    public HashMap<String, Double> calculateEMI(double principle, LoanType loanType, int time) {
        double rate = loanType.getLoanInterestRate();
        double monthlyRate = rate / 12 / 100;
        int numberOfPayments = time * 12;
        double loanEmi = (principle * monthlyRate * Math.pow(1 + monthlyRate, numberOfPayments)) / (Math.pow(1 + monthlyRate, numberOfPayments) - 1);
        double totalPayment = loanEmi * numberOfPayments;
        double totalInterestPayable = totalPayment - principle;

        // Round off the calculated values to two decimal places
        loanEmi = Math.round(loanEmi * 100.0) / 100.0;
        totalPayment = Math.round(totalPayment * 100.0) / 100.0;
        totalInterestPayable = Math.round(totalInterestPayable * 100.0) / 100.0;

        HashMap<String, Double> result = new HashMap<>();
        result.put("loanEmi", loanEmi);
        result.put("totalInterestPayable", totalInterestPayable);
        result.put("totalPayment", totalPayment);

        return result;
    }
}
