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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    SchemeServiceImplementation schemeService;

    @Override
    public ResponseEntity<?> schemeLoan(String email) {
        User user = userService.getByEmail(email);
        Accounts accounts = user.getAccounts();
        Scheme scheme = accounts.getScheme();
        if (scheme == null)
            return new ResponseEntity<>("No scheme running", HttpStatus.I_AM_A_TEAPOT);
        else{
            double schemeLoan = scheme.getMonthlyDepositAmount() * scheme.getTenure();
            return new ResponseEntity<>(schemeLoan,HttpStatus.FOUND);
        }
    }

    @Override
    public void                                                                                                                                                                                                                                                     applySchemeLoan(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        Scheme scheme = acc.getScheme();
        if (scheme == null);
//            return new ResponseEntity<>("No scheme running", HttpStatus.I_AM_A_TEAPOT);
        else {
            double schemeLoan = scheme.getMonthlyDepositAmount() * scheme.getTenure();
            SchLoanCalcDto schLoanCalcDto = new SchLoanCalcDto();
            schLoanCalcDto.setRePaymentTerm((int) ChronoUnit.DAYS.between(scheme.getStartDate(), LocalDate.now()));
            schLoanCalcDto.setPrincipalLoanAmount(schemeLoan - scheme.getTotalDepositAmount());
            schLoanCalcDto.setInterestRate(LoanType.Scheme.getLoanInterestRate());

            Loan loan = new Loan();
            loan.setLoanType(LoanType.Scheme);
            loan.setInterestRate(LoanType.Scheme.getLoanInterestRate());
            loan.setRePaymentTerm(schLoanCalcDto.getRePaymentTerm());
            loan.setPrincipalLoanAmount(schemeLoan);
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
            // return new ResponseEntity<>("Scheme Loan has been applied for", HttpStatus.I_AM_A_TEAPOT);
        }
    }

    public double calculateFirstPayableSchLoanAmount(SchLoanCalcDto schLoanCalcDto) {
        return userLoanService.calculateFirstPayableAmount(new LoanCalcDto(schLoanCalcDto));
    }

    public double calculateSchLoanEMI(SchLoanCalcDto schLoanCalcDto) {
        return userLoanService.calculateEMI(new LoanCalcDto(schLoanCalcDto));
    }
    @Override
    public ResponseEntity<?> getLoanInfo(String email) {
        User user = userService.getByEmail(email);
        Accounts accounts = user.getAccounts();
        Scheme scheme = accounts.getScheme();
        if (scheme == null) {
            return userLoanService.getLoanInfo(email);
        }
        else
            return null;
    }

    @Override
    public MonthlyEmiDto payEMI(String email) {
        User user = userService.getByEmail(email);
        Accounts accounts = user.getAccounts();
        Scheme scheme = accounts.getScheme();
        if (scheme == null) {
            return userLoanService.payEMI(email);
        }
        else {
            return null;
        }
    }

    @Override
    public ResponseEntity<?> getLoanClosureDetails(String email) {
        if(schemeService.CheckForSchemeRunning(email))
            return new ResponseEntity<>("No scheme was found running for this account", HttpStatus.NOT_FOUND);
        else
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
    public ResponseEntity<?> applyForLoanClosure(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        Scheme scheme = acc.getScheme();
        List<Loan> loanList = acc.getLoanList();
        if (scheme == null)
            return new ResponseEntity<>("No scheme running", HttpStatus.I_AM_A_TEAPOT);
        else {
            if (loanList.isEmpty())
                return new ResponseEntity<>("no Loan record found", HttpStatus.NOT_FOUND);
            else {
                for (Loan loan : loanList) {
                    if (checkForExistingLoan(email) == Boolean.FALSE || loan.getLoanType() == LoanType.Scheme) {
                        if (loan.getStatus() == LoanStatus.APPROVED || loan.getStatus() == LoanStatus.SANCTIONED) {
                            double monthlyEMI = loan.getMonthlyEMI();
                            loan.setStatus(LoanStatus.REQUESTEDFORFORECLOSURE);
                            loan.setCurrentFine(monthlyEMI / 100);
                            loan.setMonthlyEMI(loan.getPayableLoanAmount() + monthlyEMI / 100);
                            loan.setRePaymentTerm((int) ChronoUnit.DAYS.between(loan.getStartDate(), firstDateOfNextMonth(LocalDate.now())));
                        } else
                            return new ResponseEntity<>("No Approved/Sanctioned Loan Found", HttpStatus.NOT_FOUND);
                    } else
                        return new ResponseEntity<>("No running scheme loan found", HttpStatus.NOT_FOUND);
                }
            }
        }
        return new ResponseEntity<>("Applied For Closure", HttpStatus.ACCEPTED);
    }

    @Override
    public LocalDate calcFirstEMIDate(LocalDate startDate) {
        return firstDateOfNextMonth(startDate);
    }

}
