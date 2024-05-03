package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.user.serviceInterface.EmiCalculatorService;
import org.springframework.stereotype.Service;

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
    public double calculateEMI(double principal, double rate, int time) {
        double monthlyRate = rate / 12 / 100;
        int numberOfPayments = time * 12;
        double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, numberOfPayments)) / (Math.pow(1 + monthlyRate, numberOfPayments) - 1);
        return emi;
    }
}
