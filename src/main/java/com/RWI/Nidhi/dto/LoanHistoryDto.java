package com.RWI.Nidhi.dto;

import lombok.Data;

@Data
public class LoanHistoryDto {

    private Integer loanId;

	private String userName;

	private String loanType;

	private double requestedLoanAmount;

	private double interestRate;

	private double monthlyEmi;

	private String status;
}
