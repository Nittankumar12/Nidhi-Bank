package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

<<<<<<< HEAD
import com.RWI.Nidhi.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;

=======
>>>>>>> ca3fbfa0cf31b6c0a657b04ed4603f421733082a
@Repository
public interface LoanRepo extends JpaRepository<Loan, Integer> {

    LoanStatus findStatusByLoanId(int loanId);

    double findEMIByLoanId(int loanId);

    double findFineByLoanId(int loanId);

<<<<<<< HEAD
	List<Loan> findLoanByLoanType(String loanType);

	List<Loan> findLoanByStatus(String status);

	List<Loan> findByemiDateBetween(LocalDate startDate, LocalDate endDate);
	List<Loan> findByStatus(LoanStatus status);
=======
    List<Loan> findLoanByLoanType(String loanType);
>>>>>>> ca3fbfa0cf31b6c0a657b04ed4603f421733082a

    List<Loan> findLoanByStatus(String status);

    List<Loan> findByEmiDateBetween(LocalDate startDate, LocalDate endDate);
}
