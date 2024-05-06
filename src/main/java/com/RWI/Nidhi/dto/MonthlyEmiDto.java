package com.RWI.Nidhi.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MonthlyEmiDto {
    private double monthlyEMI;
    private int rePaymentTermLeft;
    private double payableLoanAmount;
    private LocalDate nextEMIDate;
    LocalDate paymentDate;
    double fine;
}