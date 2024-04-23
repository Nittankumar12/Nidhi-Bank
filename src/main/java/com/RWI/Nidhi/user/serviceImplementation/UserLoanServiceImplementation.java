package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.user.repository.LoanRepo;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

public class UserLoanServiceImplementation implements UserLoanServiceInterface {
    @Autowired
    LoanRepo loanRepository;
    @Override
    public void applyLoan(Loan loan) {// For User
        Loan currentLoan = new Loan();
        currentLoan.setLoanAmount(loan.getLoanAmount());
        currentLoan.setLoanType(loan.getLoanType());
        currentLoan.setRePaymentTerm(loan.getRePaymentTerm());
        currentLoan.setStatus(LoanStatus.APPLIED);
        loanRepository.save(currentLoan);
    }
    @Override
    public LoanStatus checkLoanStatus(int loanId) {// For User
        return loanRepository.findStatusByLoanId(loanId);
    }
    @Override
    public int checkCurrentEMI(int loanId){
        int currentEMI = loanRepository.findEMIByLoanId(loanId);
        if(loanRepository.findFineByLoanId(loanId) != 0)
            currentEMI =+ loanRepository.findFineByLoanId(loanId);
        return currentEMI;
    }
}
