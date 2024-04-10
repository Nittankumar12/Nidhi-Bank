package com.Railworld.NidhiBankMonolithic.service;

import com.Railworld.NidhiBankMonolithic.dto.LoanDto;
import com.Railworld.NidhiBankMonolithic.model.Loan;
import com.Railworld.NidhiBankMonolithic.model.LoanStatus;
import com.Railworld.NidhiBankMonolithic.repo.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    AccountService accountService;
    @Autowired
    ApplicationService applicationService;

    public void removeLoan(Integer id) {
        loanRepository.deleteById(id);
    }

    public Loan getLoan(Long accountNo) {
        return accountService.getLoan(accountNo);
    }

    public void applyLoan(LoanDto loanDto) {
        applicationService.addLoan(loanDto);
        Loan loan = new Loan();
        loan.setAccount(accountService.getAccountById(loanDto.getAccountId()));
        loan.setLoanType(loanDto.getLoanType());
        loan.setAmount(loanDto.getAmount());
        loan.setLoanStatus(LoanStatus.PENDING);
        loanRepository.save(loan);
        System.out.println("Loan added and waiting for verification");
    }

}
