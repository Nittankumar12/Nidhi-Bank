package com.RWI.Nidhi.user.repository;

import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepo extends JpaRepository<Loan, Integer> {
    LoanStatus findStatusByLoanId(int loanId);
    int findEMIByLoanId(int loanId);
    int findFineByLoanId(int loanId);
}
