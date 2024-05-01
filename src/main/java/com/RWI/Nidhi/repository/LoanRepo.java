package com.RWI.Nidhi.repository;

import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepo extends JpaRepository<Loan, Integer> {
    LoanStatus findStatusByLoanId(int loanId);
    double findEMIByLoanId(int loanId);
    double findFineByLoanId(int loanId);
}
