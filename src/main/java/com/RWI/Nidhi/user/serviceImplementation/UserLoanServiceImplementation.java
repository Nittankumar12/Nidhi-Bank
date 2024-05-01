package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.LoanApplyDto;
import com.RWI.Nidhi.dto.LoanCalcDto;
import com.RWI.Nidhi.dto.LoanInfoDto;
import com.RWI.Nidhi.dto.MonthlyEmiDto;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.Loan;

import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.LoanStatus;

import com.RWI.Nidhi.repository.LoanRepo;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class UserLoanServiceImplementation implements UserLoanServiceInterface {
    @Autowired
    LoanRepo loanRepository;
    @Autowired
    UserService userService;

    @Override
    public double maxApplicableLoan(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        return acc.getCurrentBalance() * 5;
    }
    @Override
    public void applyLoan(LoanApplyDto loanApplyDto) {// For User

        User user = userService.getByEmail(loanApplyDto.getEmail());
        Accounts acc = user.getAccounts();

        LoanCalcDto loanCalcDto = new LoanCalcDto();
        loanCalcDto.setLoanType(loanApplyDto.getLoanType());
        loanCalcDto.setRePaymentTerm(loanApplyDto.getRePaymentTerm());
        loanCalcDto.setPrincipalLoanAmount(loanApplyDto.getPrincipalLoanAmount());
        loanCalcDto.setInterestRate(loanApplyDto.getLoanType().getLoanInterestRate());

        Loan loan = new Loan();
        loan.setLoanType(loanApplyDto.getLoanType());
        loan.setInterestRate(loanApplyDto.getLoanType().getLoanInterestRate());
        loan.setRePaymentTerm(loanApplyDto.getRePaymentTerm());
        loan.setPrincipalLoanAmount(loanApplyDto.getPrincipalLoanAmount());
        loan.setStartDate(LocalDate.now());
        //Payable
        loan.setPayableLoanAmount(calculatePayableAmount(loanCalcDto));
        //MonthlyEMI
        loan.setMonthlyEMI(calculateEMI(loanCalcDto));
        //Status
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
    public Boolean checkForLoanBound(String email, double principalLoanAmount) {
        double maxLoan = maxApplicableLoan(email);
        if (principalLoanAmount > maxLoan)
            return Boolean.FALSE;
        else
            return Boolean.TRUE;
    }
    public double calculatePayableAmount(LoanCalcDto loanCalcDto){
        //Internal Methods for apply Loan, only to be used when initially
        double p = loanCalcDto.getPrincipalLoanAmount();
        double r = loanCalcDto.getLoanType().getLoanInterestRate();
        int n = loanCalcDto.getRePaymentTerm();
        loanCalcDto.setPayableLoanAmount(p*r*n*(Math.pow((1+r),n))/((Math.pow((1+r),n))-1));
        return loanCalcDto.getPayableLoanAmount();
    }
    @Override
    public double calculateEMI(LoanCalcDto loanCalcDto){
        //Internal Methods for apply Loan
        double p = loanCalcDto.getPrincipalLoanAmount();
        double r = loanCalcDto.getLoanType().getLoanInterestRate();
        int n = loanCalcDto.getRePaymentTerm();
        loanCalcDto.setMonthlyEMI(p*r*(Math.pow((1+r),n))/((Math.pow((1+r),n))-1));
        return loanCalcDto.getMonthlyEMI();
    }
    @Override
    public LoanInfoDto getLoanInfo(String email){
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        Loan loan = acc.getLoan();
        LoanInfoDto loanInfoDto = new LoanInfoDto();
        loanInfoDto.setLoanType(loan.getLoanType());
        loanInfoDto.setPrincipalLoanAmount(loan.getPrincipalLoanAmount());
        loanInfoDto.setStatus(loan.getStatus());
        loanInfoDto.setInterestRate(loan.getInterestRate());
        loanInfoDto.setPayableLoanAmount(loan.getPayableLoanAmount());
        loanInfoDto.setEmail(email);
        loanInfoDto.setMonthlyEMI(loan.getMonthlyEMI());
        loanInfoDto.setFine(loan.getFine());
        loanInfoDto.setStartDate(loan.getStartDate());
        loanInfoDto.setRePaymentTerm(loan.getRePaymentTerm());
        return loanInfoDto;
    }
    @Override
    public MonthlyEmiDto payEMI(String email){
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        Loan loan = acc.getLoan();

        MonthlyEmiDto monthlyEmiDto = new MonthlyEmiDto();

        double payableLoanAmount = loan.getPayableLoanAmount();
        double temp = payableLoanAmount;
        payableLoanAmount = temp - loan.getMonthlyEMI();
        loan.setPayableLoanAmount(payableLoanAmount);
        LocalDate endDate = ChronoUnit.DAYS.addTo(loan.getStartDate(), loan.getRePaymentTerm());
        int rePaymentTermLeft = (int) ChronoUnit.DAYS.between(endDate,LocalDate.now());

        monthlyEmiDto.setPayableLoanAmount(payableLoanAmount);
        monthlyEmiDto.setMonthlyEMI(loan.getMonthlyEMI());
        monthlyEmiDto.setRePaymentTermLeft(rePaymentTermLeft);
        monthlyEmiDto.setPaymentDate(LocalDate.now());
        return monthlyEmiDto;
        // In return - EMI paid, EMI month, Months left, amount left
    }
}
