package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.LoanDto;
import com.RWI.Nidhi.dto.LoanHIstoryDTO;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.Loan;

import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;

import com.RWI.Nidhi.repository.LoanRepo;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoanServiceImplementation implements UserLoanServiceInterface {
	@Autowired
	LoanRepo loanRepository;
	@Autowired
	UserService userService;

	@Override
	public double maxApplicableLoan(String email) {
		User user = userService.getByEmail(email);
		Accounts acc = user.getAccounts();
		return acc.getCurrentBalance() * 5;
	}

	@Override
	public void applyLoan(LoanDto loanDto) {// For User

		User user = userService.getByEmail(loanDto.getEmail());
		Accounts acc = user.getAccounts();
		Loan loan = new Loan();
		loan.setLoanType(loanDto.getLoanType());
		loan.setRePaymentTerm(loanDto.getRePaymentTerm());
		loan.setPrincipalLoanAmount(loanDto.getPrincipalLoanAmount());
		loan.setStartDate(loanDto.getStartDate());
		loan.setInterestRate(loanDto.getLoanType().getLoanInterestRate());
		// Payable
		loanDto.setPayableLoanAmount(calculatePayableAmount(loanDto));
		loan.setPayableLoanAmount(loanDto.getPayableLoanAmount());
		// EMI
		loanDto.setMonthlyEMI(calculateEMI(loanDto));
		loan.setMonthlyEMI(loanDto.getMonthlyEMI());

		loan.setStatus(LoanStatus.APPLIED);
		loan.setAccount(acc);// save acc in loan
		acc.setLoan(loan);// save loan in acc
		loanRepository.save(loan);// save loan in loan

	}

	@Override
	public Boolean checkForExistingLoan(String email) {
		User user = userService.getByEmail(email);
		Accounts acc = user.getAccounts();
		Loan loan = acc.getLoan();
		if (loan == null)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}

	@Override
	public Boolean checkForLoanBound(String email, double principalLoanAmount) {
		double maxLoan = maxApplicableLoan(email);
		if (principalLoanAmount > maxLoan)
			return Boolean.FALSE;
		else
			return Boolean.TRUE;

	}

	@Override
	public LoanDto getLoanInfo(String email) {
		User user = userService.getByEmail(email);
		Accounts acc = user.getAccounts();
		Loan loan = acc.getLoan();
		LoanDto loanDto = new LoanDto();
		loanDto.setLoanType(loan.getLoanType());
		loanDto.setPrincipalLoanAmount(loan.getPrincipalLoanAmount());
		loanDto.setStatus(loan.getStatus());
		loanDto.setInterestRate(loan.getInterestRate());
		loanDto.setPayableLoanAmount(loan.getPayableLoanAmount());
		loanDto.setEmail(email);
		loanDto.setMonthlyEMI(loan.getMonthlyEMI());
		loanDto.setFine(loan.getFine());
		loanDto.setStartDate(loan.getStartDate());
		loanDto.setRePaymentTerm(loan.getRePaymentTerm());
		return loanDto;
	}

	@Override
	public double calculatePayableAmount(LoanDto loanDto) {// Internal Methods for apply Loan, only to be used when
															// initially
		double p = loanDto.getPrincipalLoanAmount();
		double r = loanDto.getInterestRate();
		int n = loanDto.getRePaymentTerm();
		double payable = p * r * n * (Math.pow((1 + r), n)) / ((Math.pow((1 + r), n)) - 1);
		return payable;
	}

	@Override
	public double calculateEMI(LoanDto loanDto) {// Internal Methods for apply Loan
		double p = loanDto.getPrincipalLoanAmount();
		double r = loanDto.getInterestRate();
		int n = loanDto.getRePaymentTerm();
		double monthlyEMI = p * r * (Math.pow((1 + r), n)) / ((Math.pow((1 + r), n)) - 1);
		return monthlyEMI;
	}

	@Override
	public Boolean payEMI(String email, double payedAmount) {
		User user = userService.getByEmail(email);
		Accounts acc = user.getAccounts();
		Loan loan = acc.getLoan();
		double payable;
		double temp = loan.getPayableLoanAmount();
		if (loan.getMonthlyEMI() == payedAmount) {
			payable = temp - payedAmount;
			loan.setPayableLoanAmount(payable);
			return Boolean.TRUE;
		} else
			return Boolean.FALSE;
	}

	@Override
	public List<LoanHIstoryDTO> getLoansByLoanType(String loanType) {
		Loan loan1 = new Loan();
		List<Loan> loans = loanRepository.findAll().stream().filter((loan) -> loan1.getLoanType().equals(loanType))
				.collect(Collectors.toList());
		List<LoanHIstoryDTO> loanDTOList = new ArrayList<>();
		for (Loan loan : loans) {
//			User user = new User();
			LoanHIstoryDTO loanhistortDTO = new LoanHIstoryDTO();

			loanhistortDTO.setLoanId(loan.getLoanId());
			// loanhistortDTO.setLoanType(loanType);

			loanhistortDTO.setRequestedLoanAmount(loan.getPrincipalLoanAmount());
			loanhistortDTO.setInterestRate(loan.getInterestRate());
			loanhistortDTO.setMonthlyEmi(loan.getMonthlyEMI());
			loanhistortDTO.setUserName(loan.getAccount().getUser().getUserName());
			loanDTOList.add(loanhistortDTO);

		}
		return loanDTOList;

	}

	@Override
	public List<LoanHIstoryDTO> getLoansByLoanStatus(String status) {
		// TODO Auto-generated method stub
		return null;
	}

}
