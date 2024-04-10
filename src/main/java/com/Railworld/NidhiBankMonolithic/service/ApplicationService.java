package com.Railworld.NidhiBankMonolithic.service;

import com.Railworld.NidhiBankMonolithic.dto.LoanDto;
import com.Railworld.NidhiBankMonolithic.model.Application;
import com.Railworld.NidhiBankMonolithic.model.Loan;
import com.Railworld.NidhiBankMonolithic.repo.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {
    @Autowired
    AccountService accountService;
    @Autowired
    ApplicationRepository applicationRepository;
    public void addLoan(LoanDto loanDto) {
        Application application = new Application();
        application.setAccount(accountService.getAccountById(loanDto.getAccountId()));
        application.setType("Loan");
        applicationRepository.save(application);
        System.out.println("Application submitted:" + application.toString());
    }

    public List<Application> getApplications() {
        return applicationRepository.findAll();
    }

    public void removeApplication(int accountId) {
        System.out.println("application is: " + accountService.getAccountById(accountId).getApplications().get(0));
        Application application =  accountService.getAccountById(accountId).getApplications().get(0);
        System.out.println("application id is: "  + application.getId());
        applicationRepository.deleteById(application.getId());
        System.out.println("application deleted");
    }
}
