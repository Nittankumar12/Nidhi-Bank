package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.SchLoanCalcDto;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.entity.Scheme;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.LoanType;
import com.RWI.Nidhi.repository.LoanRepo;
import com.RWI.Nidhi.user.serviceInterface.UserSchemeLoanServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class UserSchemeLoanServiceImplementation implements UserSchemeLoanServiceInterface{
    @Autowired
    LoanRepo loanRepo;
    @Autowired
    UserService userService;
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
        schLoanCalcDto.setRePaymentTerm((int) ChronoUnit.DAYS.between(scheme.getStartDate(),LocalDate.now()));
        schLoanCalcDto.setPrincipalLoanAmount(schemeLoan(email)-scheme.getTotalDepositAmount());
        schLoanCalcDto.setInterestRate(LoanType.Scheme.getLoanInterestRate());

        Loan loan = new Loan();
        loan.setLoanType(LoanType.Scheme);
        loan.setInterestRate(LoanType.Scheme.getLoanInterestRate());
        loan.setRePaymentTerm(schLoanCalcDto.getRePaymentTerm());
        loan.setPrincipalLoanAmount(schemeLoan(email));
        loan.setStartDate(LocalDate.now());
        //Payable
        loan.setPayableLoanAmount(calculatePayableSchLoanAmount(schLoanCalcDto));
        //MonthlyEMI
        loan.setMonthlyEMI(calculateSchLoanEMI(schLoanCalcDto));
        loan.setStatus(LoanStatus.APPLIED);
        loan.setAccount(acc);
        acc.setLoan(loan);
        loanRepo.save(loan);
    }
    public double calculatePayableSchLoanAmount(SchLoanCalcDto schLoanCalcDto){
        double p = schLoanCalcDto.getPrincipalLoanAmount();
        int n = schLoanCalcDto.getRePaymentTerm();
        double r = schLoanCalcDto.getInterestRate();
        schLoanCalcDto.setPayableLoanAmount(p*r*n*(Math.pow((1+r),n))/((Math.pow((1+r),n))-1));
        return schLoanCalcDto.getPayableLoanAmount();
    }
    public double calculateSchLoanEMI(SchLoanCalcDto schLoanCalcDto){
        double p = schLoanCalcDto.getPrincipalLoanAmount();
        int n = schLoanCalcDto.getRePaymentTerm();
        double r = schLoanCalcDto.getInterestRate();
        schLoanCalcDto.setPayableLoanAmount(p*r*n*(Math.pow((1+r),n))/((Math.pow((1+r),n))-1));
        return schLoanCalcDto.getPayableLoanAmount();
    }
    public Boolean checkForExistingLoan(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        Loan loan = acc.getLoan();
        if (loan == null)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}
