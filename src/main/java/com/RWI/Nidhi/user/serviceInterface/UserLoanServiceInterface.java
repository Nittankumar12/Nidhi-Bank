package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.LoanDto;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;

public interface UserLoanServiceInterface {
    void applyLoan(LoanDto loanDto);
    LoanStatus checkLoanStatus(int loanId);
    int checkCurrentEMI(int loanId);
    public String payEMI(int loanId, int payedEMI);
    public void applyLoanClosure(int loanId);
}
