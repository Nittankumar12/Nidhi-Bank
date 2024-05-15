package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.enums.LoanType;
import com.RWI.Nidhi.user.serviceInterface.EmiCalculatorService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;

@Service
public class EmiCalculatorServiceImplementation implements EmiCalculatorService {
//    @Override
//    public double calculateEMI(double principal, double rate, int time) {
//        rate = rate / 100 / 12; // convert monthly rate and time to decimal
//        time = time * 12;
//        double EMI = principal * rate * Math.pow(1 + rate, time) / (Math.pow(1 + rate, time) - 1);
//        return EMI;
//    }

    @Override
    public double[] calculateEMI(double principle, LoanType loanType, int time) {
        double rate = loanType.getLoanInterestRate();
        double monthlyRate = rate / 12 / 100;
        int numberOfPayments = time * 12;
        double emi = (principle * monthlyRate * Math.pow(1 + monthlyRate, numberOfPayments)) / (Math.pow(1 + monthlyRate, numberOfPayments) - 1);
        double totalPayment = emi * numberOfPayments;
        double totalInterestPayable = totalPayment - principle;
        return new double[]{emi, totalInterestPayable, totalPayment};
    }
}
