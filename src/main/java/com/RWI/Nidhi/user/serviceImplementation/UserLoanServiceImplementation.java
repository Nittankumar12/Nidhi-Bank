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

import java.util.List;

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
    public void applyLoan(LoanDto loanDto) {// For User

        User user = userService.getByEmail(loanDto.getEmail());
        Accounts acc = user.getAccounts();
        List<Loan> loanList= acc.getLoanList();
        Loan loan = new Loan();
        loan.setLoanType(loanDto.getLoanType());
        loan.setRePaymentTerm(loanDto.getRePaymentTerm());
        loan.setPrincipalLoanAmount(loanDto.getPrincipalLoanAmount());
        loan.setStartDate(loanDto.getStartDate());
        loan.setStatus(LoanStatus.APPLIED);
        loan.setAccount(acc);// save acc in loan
        loanList.add(loan);
        acc.setLoanList(loanList);// save loan in acc
        loanRepository.save(loan);//save loan in loan
    }
    @Override
    public Boolean checkForLoan(String email){
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        List<Loan> loan= acc.getLoanList();
        if(loan.isEmpty()==Boolean.TRUE)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}
