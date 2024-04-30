package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.LoanDto;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.Loan;

import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;

import com.RWI.Nidhi.repository.LoanRepo;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoanServiceImplementation implements UserLoanServiceInterface {
    @Autowired
    LoanRepo loanRepository;
    @Autowired
    AccountsServiceImplementation accountsService;
    @Autowired
    SchemeServiceImplementation schemeService;
    @Autowired
    UserService userService;

    @Override
    public double maxApplicableLoan(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        double maxApplicableLoan = acc.getCurrentBalance() * 5;
        return maxApplicableLoan;
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
        loan.setStatus(LoanStatus.APPLIED);
        loan.setAccount(acc);// save acc in loan
        acc.setLoan(loan);// save loan in acc
        loanRepository.save(loan);//save loan in loan
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
    public Boolean checkForLoanBound(String email, int principalLoanAmount) {
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
        loanDto.setEMI(loan.getEMI());
        loanDto.setFine(loan.getFine());
        loanDto.setStartDate(loan.getStartDate());
        loanDto.setRePaymentTerm(loan.getRePaymentTerm());
        return loanDto;
    }
}
