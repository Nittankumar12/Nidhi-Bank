package com.RWI.Nidhi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;

@Repository
public interface LoanRepo extends JpaRepository<Loan, Integer> {

	LoanStatus findStatusByLoanId(int loanId);

	double findEMIByLoanId(int loanId);

	double findFineByLoanId(int loanId);

	List<Loan> findLoanByLoanType(String loanType);

	List<Loan> findLoanByStatus(String status);

	List<Loan> findByemiDateBetween(LocalDate startDate, LocalDate endDate);


}
