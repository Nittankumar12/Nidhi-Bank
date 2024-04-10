package com.Railworld.NidhiBankMonolithic.service;

import com.Railworld.NidhiBankMonolithic.model.*;
import com.Railworld.NidhiBankMonolithic.repo.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    MemberService memberService;

    @Autowired
    ApplicationService applicationService;
    @Autowired
    AccountService accountService;
    @Autowired
    CompanyRepository companyRepository;
    public Company getById(Integer companyId) {
        return companyRepository.findById(companyId).get();
    }
    public List<Member> getMembers(int id) {
        List<Member> members = memberService.getMembers(id);
        return members;
    }

    public List<Application> getApplications() {
        return applicationService.getApplications();
    }

    public void updateLoanStatus(Integer accountId,String loanStatus) {
        Account account = accountService.getAccountById(accountId);
        Loan loan = account.getLoan();

        if(loanStatus.equals("ACCEPTED")) loan.setLoanStatus(LoanStatus.APPROVED);
        else if(loanStatus.equals("REJECTED")) loan.setLoanStatus(LoanStatus.REJECTED);
        else loan.setLoanStatus(LoanStatus.HOLD);

        LoanStatus currStatus = loan.getLoanStatus();
        if(currStatus.equals(LoanStatus.APPROVED)){
            System.out.println("We have to disburse amount");
            accountService.disburseAmount(loan.getAmount(),account);
        }
        else if(currStatus.equals(LoanStatus.HOLD)){
            System.out.println("Loan Approval on Hold");
        }
        else{
            System.out.println("Loan Rejected");

        }
    }
}
