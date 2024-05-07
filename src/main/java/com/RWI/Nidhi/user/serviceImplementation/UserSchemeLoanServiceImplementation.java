package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.*;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.LoanType;
import com.RWI.Nidhi.repository.LoanRepo;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserSchemeLoanServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserSchemeLoanServiceImplementation implements UserSchemeLoanServiceInterface {
    @Autowired
    LoanRepo loanRepo;
    @Autowired
    UserService userService;
    @Autowired
    UserLoanServiceInterface userLoanService;

    @Override
    public double schemeLoan(String email) {
        User user = userService.getByEmail(email);
        Accounts accounts = user.getAccounts();
        Scheme scheme = accounts.getScheme();
        double schemeLoan = scheme.getMonthlyDepositAmount() * scheme.getTenure();
        return schemeLoan;
    }

    @Override
    public void applySchemeLoan(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        Scheme scheme = acc.getScheme();

        SchLoanCalcDto schLoanCalcDto = new SchLoanCalcDto();
        schLoanCalcDto.setRePaymentTerm((int) ChronoUnit.DAYS.between(scheme.getStartDate(), LocalDate.now()));
        schLoanCalcDto.setPrincipalLoanAmount(schemeLoan(email) - scheme.getTotalDepositAmount());
        schLoanCalcDto.setInterestRate(LoanType.Scheme.getLoanInterestRate());

        Loan loan = new Loan();
        loan.setLoanType(LoanType.Scheme);
        loan.setInterestRate(LoanType.Scheme.getLoanInterestRate());
        loan.setRePaymentTerm(schLoanCalcDto.getRePaymentTerm());
        loan.setPrincipalLoanAmount(schemeLoan(email));
        loan.setStartDate(LocalDate.now());
        loan.setEmiDate(calcFirstEMIDate(loan.getStartDate()));
        //Payable
        loan.setPayableLoanAmount(calculateFirstPayableSchLoanAmount(schLoanCalcDto));
        //MonthlyEMI
        loan.setMonthlyEMI(calculateSchLoanEMI(schLoanCalcDto));
        loan.setStatus(LoanStatus.APPLIED);
        loan.setAccount(acc);
        List<Loan> loanList = new ArrayList<>();
        loanList.add(loan);
        acc.setLoanList(loanList);// save loan in acc
        loanRepo.save(loan);
    }

    public double calculateFirstPayableSchLoanAmount(SchLoanCalcDto schLoanCalcDto) {
        return userLoanService.calculateFirstPayableAmount(new LoanCalcDto(schLoanCalcDto));
    }

    public double calculateSchLoanEMI(SchLoanCalcDto schLoanCalcDto) {
        return userLoanService.calculateEMI(new LoanCalcDto(schLoanCalcDto));
    }

    @Override
    public LoanInfoDto getLoanInfo(String email) {
        return userLoanService.getLoanInfo(email);
    }

    @Override
    public MonthlyEmiDto payEMI(String email) {
        return userLoanService.payEMI(email);
    }

    @Override
    public LoanClosureDto getLoanClosureDetails(String email) {
        return userLoanService.getLoanClosureDetails(email);
    }

    @Override
    public Boolean checkForExistingLoan(String email) {
        return userLoanService.checkForExistingLoan(email);
    }

    @Override
    public LocalDate firstDateOfNextMonth(LocalDate date) {
        LocalDate nextMonth = date.plusMonths(1);
        return nextMonth.withDayOfMonth(1);
    }

    @Override
    public String applyForLoanClosure(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        List<Loan> loanList = acc.getLoanList();
        for (int i = 0; i < loanList.size(); i++) {
            if (checkForExistingLoan(email) == Boolean.FALSE) {
                if (loanList.get(i).getStatus() == LoanStatus.APPROVED || loanList.get(i).getStatus() == LoanStatus.SANCTIONED) {
                    double monthlyEMI = loanList.get(i).getMonthlyEMI();
                    loanList.get(i).setStatus(LoanStatus.REQUESTEDFORFORECLOSURE);
                    loanList.get(i).setCurrentFine(monthlyEMI / 100);
                    loanList.get(i).setMonthlyEMI(loanList.get(i).getPayableLoanAmount() + monthlyEMI / 100);
                    loanList.get(i).setRePaymentTerm((int) ChronoUnit.DAYS.between(loanList.get(i).getStartDate(), firstDateOfNextMonth(LocalDate.now())));
                } else
                    return "Error";
            } else
                return "Error";
        }
        return "Applied For Closure";
    }

    @Override
    public LocalDate calcFirstEMIDate(LocalDate startDate) {
        return firstDateOfNextMonth(startDate);
    }

}
