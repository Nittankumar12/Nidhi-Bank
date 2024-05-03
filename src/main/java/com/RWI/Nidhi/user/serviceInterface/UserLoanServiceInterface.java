package com.RWI.Nidhi.user.serviceInterface;


import java.util.List;

import com.RWI.Nidhi.dto.LoanDto;
import com.RWI.Nidhi.dto.LoanHIstoryDTO;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;

public interface UserLoanServiceInterface {
	double maxApplicableLoan(String email);

	void applyLoan(LoanDto loanDto);

	Boolean checkForExistingLoan(String email);

	Boolean checkForLoanBound(String email, double principalLoanAmount);

	LoanDto getLoanInfo(String email);

	double calculatePayableAmount(LoanDto loanDto);

	double calculateEMI(LoanDto loanDto);

	Boolean payEMI(String email, double payedAmount);
	
	List <LoanHIstoryDTO> getLoansByLoanType(String loanType);
	
	List <LoanHIstoryDTO>getLoansByLoanStatus(String status);

//	public List<Loan> getLoansByLoanType(String loanType);
//
//	public List<Loan> getLoansByLoanStatus(String status);
	


import com.RWI.Nidhi.dto.*;

import java.time.LocalDate;

public interface UserLoanServiceInterface {
    double maxApplicableLoan(String email);
    Boolean checkForExistingLoan(String email);
    Boolean checkForLoanBound(String email, double principalLoanAmount);
    double calculateFirstPayableAmount(LoanCalcDto loanCalcDto);
    double calculateEMI(LoanCalcDto loanCalcDto);
    void applyLoan(LoanApplyDto loanApplyDto);
    LoanInfoDto getLoanInfo(String email);
    MonthlyEmiDto payEMI(String email);
    LoanClosureDto getLoanClosureDetails(String email);
    LocalDate firstDateOfNextMonth(LocalDate date);
    String applyForLoanClosure(String email);
    LocalDate calcFirstEMIDate(LocalDate startDate);

}
