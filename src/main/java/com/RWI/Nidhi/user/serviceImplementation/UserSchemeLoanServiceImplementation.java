package com.RWI.Nidhi.user.serviceImplementation;
import com.RWI.Nidhi.dto.*;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.CommissionType;
import com.RWI.Nidhi.enums.LoanStatus;
import com.RWI.Nidhi.enums.LoanType;
import com.RWI.Nidhi.enums.SchemeStatus;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.user.serviceInterface.AccountsServiceInterface;
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
import java.util.HashMap;
import java.util.List;
@Service
public class UserSchemeLoanServiceImplementation implements UserSchemeLoanServiceInterface {
    @Autowired
    LoanRepo loanRepo;
    @Autowired
    EmiCalculatorServiceImplementation emiCalculatorServiceImplementation;
    @Autowired
    AccountsRepo accountsRepo;
    @Autowired
    AgentRepo agentRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    CommissionRepository commissionRepo;
    @Autowired
    UserService userService;
    @Autowired
    UserLoanServiceInterface userLoanService;
    @Autowired
    AccountsServiceInterface accountsService;
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
    public ResponseEntity<?> applySchemeLoan(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        if(acc.getLoanList()==null)acc.setLoanList(new ArrayList<>());
        Agent agent = user.getAgent();
        if(agent.getLoanList()==null)acc.setLoanList(new ArrayList<>());
        if (accountsService.CheckAccStatus(user.getEmail()) == Boolean.FALSE) {
            return new ResponseEntity<>("Account not opened", HttpStatus.BAD_REQUEST);
        } else {
            Scheme scheme = acc.getScheme();
            if (scheme == null) {
                return new ResponseEntity<>("No scheme running", HttpStatus.I_AM_A_TEAPOT);
            } else {
                double schemeLoan = scheme.getMonthlyDepositAmount() * scheme.getTenure();
                SchLoanCalcDto schLoanCalcDto = new SchLoanCalcDto();
                schLoanCalcDto.setRePaymentTerm((scheme.getTenure()- ((int)ChronoUnit.DAYS.between(scheme.getStartDate(), LocalDate.now()))));
                schLoanCalcDto.setPrincipalLoanAmount(schemeLoan - scheme.getTotalDepositAmount());

                Loan loan = new Loan();
                loan.setLoanType(LoanType.Scheme);
                loan.setInterestRate(LoanType.Scheme.getLoanInterestRate());
                loan.setRePaymentTerm((scheme.getTenure()- ((int)ChronoUnit.DAYS.between(scheme.getStartDate(), LocalDate.now()))));
                loan.setPrincipalLoanAmount(schemeLoan);
                schLoanCalcDto.setPrincipalLoanAmount(schemeLoan - scheme.getTotalDepositAmount());

                //Commission
                Commission commission = new Commission();
                commission.setAgent(agent);
                commission.setUser(user);
                commission.setCommissionType((CommissionType.SchemeLoan));
                commission.setCommissionRate(CommissionType.SchemeLoan.getCommissionRate());
                commission.setCommissionAmount(accountsService.amountCalc(CommissionType.SchemeLoan.getCommissionRate(), loan.getPrincipalLoanAmount()));
                commission.setCommDate(LocalDate.now());
                commissionRepo.save(commission);

                //Status
                loan.setStatus(LoanStatus.APPLIED);
                loan.setAccount(acc);// save acc in loan
                loan.setAgent(agent);
                loan.setUser(user);
                acc.getLoanList().add(loan);
                user.setAccounts(acc);
                loanRepo.save(loan);//save loan in loan
                accountsRepo.save(acc);
                userRepo.save(user);
                agent.getLoanList().add(loan);
                agentRepo.save(agent);
                LoanInfoDto loanInfoDto = new LoanInfoDto();
                loanInfoDto.setLoanType(loan.getLoanType());
                loanInfoDto.setPayableLoanAmount(loan.getPayableLoanAmount());
                loanInfoDto.setPrincipalLoanAmount(loan.getPrincipalLoanAmount());
                loanInfoDto.setInterestRate(loan.getInterestRate());
                loanInfoDto.setStatus(loan.getStatus());
                loanInfoDto.setMonthlyEMI(loan.getMonthlyEMI());
                loanInfoDto.setRePaymentTerm(loan.getRePaymentTerm());
                loanInfoDto.setStartDate(loan.getStartDate());
                return new ResponseEntity<>(loanInfoDto,HttpStatus.OK);
            }
        }
    }
    @Override
    public double calculateFirstPayableSchLoanAmount(SchLoanCalcDto schLoanCalcDto) {
        double p = schLoanCalcDto.getPrincipalLoanAmount();
        LoanType t = schLoanCalcDto.getLoanType();
        int n = schLoanCalcDto.getRePaymentTerm();
        HashMap<String, Double> map = emiCalculatorServiceImplementation.calculateEMI(p,t,n);
        return map.get("totalPayment");
    }
    @Override
    public double calculateSchLoanEMI(SchLoanCalcDto schLoanCalcDto) {
        double p = schLoanCalcDto.getPrincipalLoanAmount();
        LoanType t = schLoanCalcDto.getLoanType();
        int n = schLoanCalcDto.getRePaymentTerm();
        HashMap<String, Double> map = emiCalculatorServiceImplementation.calculateEMI(p,t,n);
        return map.get("loanEmi");
    }
    @Override
    public ResponseEntity<?> getLoanInfo(String email) {
        User user = userService.getByEmail(email);
        Accounts accounts = user.getAccounts();
        Scheme scheme = accounts.getScheme();
        if (scheme != null) {
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
        if (scheme != null && scheme.getSStatus().equals(SchemeStatus.APPLIEDFORLOAN)) {
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
        return userLoanService.isLoanNotOpen(email);
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
                    if (checkForExistingLoan(email) == Boolean.FALSE && loan.getLoanType() == LoanType.Scheme) {
                        if (loan.getStatus().equals(LoanStatus.APPROVED) || loan.getStatus().equals(LoanStatus.SANCTIONED)) {
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