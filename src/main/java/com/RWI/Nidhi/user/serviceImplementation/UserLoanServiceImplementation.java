package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.*;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.*;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.user.serviceInterface.AccountsServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserLoanServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class UserLoanServiceImplementation implements UserLoanServiceInterface {
    @Autowired
    LoanRepo loanRepository;
    @Autowired
    EmiService emiService;
//    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    CommissionRepository commissionRepo;
    @Autowired
    AgentRepo agentRepo;
    @Autowired
    UserService userService;
    @Autowired
    TransactionRepo transactionRepo;
    @Autowired
    AccountsServiceInterface accountsService;
    @Autowired
    AccountsRepo accountsRepo;
    @Autowired
    UserPenaltyServiceImplementation penaltyService;
    @Autowired
    EmiCalculatorServiceImplementation emiCalculatorServiceImplementation;
    @Autowired
    UserRepo userRepo;
    @Autowired
    PenaltyRepo penaltyRepo;

    @Override
    public double maxApplicableLoan(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        return acc.getCurrentBalance() * 5;
    }

    @Override
    public LoanInfoDto applyLoan(LoanApplyDto loanApplyDto) {
        User user = userService.getByEmail(loanApplyDto.getUserEmail());
        Accounts acc = user.getAccounts();
        if (acc.getLoanList() == null) acc.setLoanList(new ArrayList<>());
        Agent agent = user.getAgent();
        if (agent.getLoanList() == null) acc.setLoanList(new ArrayList<>());
        if (accountsService.CheckAccStatus(user.getEmail()) == Boolean.FALSE) {
            return null;
        } else {
            Loan loan = new Loan();
            loan.setLoanType(loanApplyDto.getLoanType());
            loan.setInterestRate(loanApplyDto.getLoanType().getLoanInterestRate());
            loan.setRePaymentTerm(loanApplyDto.getRePaymentTerm());
            loan.setPrincipalLoanAmount(loanApplyDto.getPrincipalLoanAmount());
            loan.setStatus(LoanStatus.APPLIED);

            //Commission
            Commission commission = new Commission();
            commission.setAgent(agent);
            commission.setUser(user);
            commission.setCommissionType((CommissionType.valueOf(loanApplyDto.getLoanType() + "Loan")));
            commission.setCommissionRate(CommissionType.valueOf(loanApplyDto.getLoanType() + "Loan").getCommissionRate());
            commission.setCommissionAmount(accountsService.amountCalc(CommissionType.valueOf(loanApplyDto.getLoanType() + "Loan").getCommissionRate(), loan.getPrincipalLoanAmount()));
            commission.setCommDate(LocalDate.now());
            commissionRepo.save(commission);

            loan.setAccount(acc);
            loan.setAgent(agent);
            loan.setUser(user);
            acc.getLoanList().add(loan);
            user.setAccounts(acc);
            loanRepository.save(loan);

            // Send notification to admin
            String notificationMessage = "User " + user.getUserName() + " has applied for a loan";
            simpMessagingTemplate.convertAndSend("/topic/admin", notificationMessage);
            // Send notification to user
            String notificationMsg = "User " + user.getUserName() + " has applied for a loan";
            simpMessagingTemplate.convertAndSend("/topic/user", notificationMsg);


            accountsRepo.save(acc);
            userRepo.save(user);
            agent.getLoanList().add(loan);
            agentRepo.save(agent);
            LoanInfoDto loanInfoDto = new LoanInfoDto();

            if (loan.getLoanType().equals(LoanType.Other)) {
                EmiDetails emiDetails = emiService.calculateEmi(loan.getPrincipalLoanAmount(), loan.getDiscount(), loan.getRePaymentTerm());
                loanInfoDto.setUserEmail(user.getEmail());
                loanInfoDto.setLoanType(loan.getLoanType());
                loanInfoDto.setPayableLoanAmount(emiDetails.getCustomerPrice());
                loanInfoDto.setPrincipalLoanAmount(emiDetails.getMrpPrice());
                loanInfoDto.setDiscount(emiDetails.getDiscount());
                loanInfoDto.setMonthlyEMI(emiDetails.getEmi9Months());
                if (emiDetails.getEmi9Months() == 0) {
                    loanInfoDto.setRePaymentTerm(12);
                } else if (emiDetails.getEmi12Months() == 0) {
                    loanInfoDto.setRePaymentTerm(9);
                }
                loanInfoDto.setStartDate(loan.getStartDate());
                loanInfoDto.setStatus(loan.getStatus());
                return loanInfoDto;
            }
            else {
                loanInfoDto.setUserEmail(user.getEmail());
                loanInfoDto.setLoanType(loan.getLoanType());

                LoanCalcDto loanCalcDto = new LoanCalcDto();
                loanCalcDto.setLoanType(loan.getLoanType());
                loanCalcDto.setRePaymentTerm(loan.getRePaymentTerm());
                loanCalcDto.setPrincipalLoanAmount(loan.getPrincipalLoanAmount());
                loanCalcDto.setInterestRate(loan.getLoanType());

                loanInfoDto.setMonthlyEMI(calculateEMI(loanCalcDto));
                loanInfoDto.setPayableLoanAmount(calculateFirstPayableAmount(loanCalcDto));
                loanInfoDto.setPayableLoanAmount(loan.getPayableLoanAmount());
                loanInfoDto.setPrincipalLoanAmount(loan.getPrincipalLoanAmount());
                loanInfoDto.setInterestRate(loan.getInterestRate());
                loanInfoDto.setStatus(loan.getStatus());
                loanInfoDto.setMonthlyEMI(loan.getMonthlyEMI());
                loanInfoDto.setRePaymentTerm(loan.getRePaymentTerm());
                loanInfoDto.setStartDate(loan.getStartDate());
                return loanInfoDto;
            }
        }
    }

    @Override
    public LocalDate calcFirstEMIDate(LocalDate startDate) {
        return firstDateOfNextMonth(startDate);
    }

    @Override
    public List<LoanHistoryDto> getLoansByLoanType(String loanType) {
        return null;
    }

    @Override
    public List<LoanHistoryDto> getLoansByLoanStatus(String status) {
        return null;
    }

    @Override
    public Boolean isLoanNotOpen(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        Boolean b = Boolean.FALSE;
        List<Loan> loanList = acc.getLoanList();
        if (loanList.isEmpty() == Boolean.TRUE) {
            return Boolean.TRUE;
        }
        for (Loan loan : loanList) {
            if (loan.getStatus().equals(LoanStatus.CLOSED) || loan.getStatus().equals(LoanStatus.FORECLOSED) || loan.getStatus().equals(LoanStatus.REJECTED)) {
                b = Boolean.TRUE;
            } else {
                b = Boolean.FALSE;
            }
        }
        return b;
    }

    @Override
    public Boolean checkForLoanBound(String email, double principalLoanAmount) {
        double maxLoan = maxApplicableLoan(email);
        if (principalLoanAmount > maxLoan)
            return Boolean.FALSE;
        else
            return Boolean.TRUE;
    }

    public double calculateFirstPayableAmount(LoanCalcDto loanCalcDto) {
        double p = loanCalcDto.getPrincipalLoanAmount();
        LoanType t = loanCalcDto.getLoanType();
        int n = loanCalcDto.getRePaymentTerm();
        HashMap<String, Double> map = emiCalculatorServiceImplementation.calculateEMI(p,t,n);
        return map.get("totalPayment");
    }

    @Override
    public double calculateEMI(LoanCalcDto loanCalcDto) {
        double p = loanCalcDto.getPrincipalLoanAmount();
        LoanType t = loanCalcDto.getLoanType();
        int n = loanCalcDto.getRePaymentTerm();
        HashMap<String, Double> map = emiCalculatorServiceImplementation.calculateEMI(p,t,n);
        return map.get("loanEmi");
    }
    @Override
    public ResponseEntity<?> getLoanInfo(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        LoanInfoDto loanInfoDto = new LoanInfoDto();
        List<Loan> loanList = acc.getLoanList();
        if (loanList.isEmpty())
            return new ResponseEntity<>("No loan found", HttpStatus.NOT_FOUND);
        else {
            for (Loan loan : loanList) {
                if (isLoanNotOpen(email) == Boolean.FALSE) {
                    loanInfoDto.setLoanType(loan.getLoanType());

                    loanInfoDto.setPrincipalLoanAmount(loan.getPrincipalLoanAmount());
                    loanInfoDto.setStatus(loan.getStatus());
                    loanInfoDto.setInterestRate(loan.getInterestRate());
                    loanInfoDto.setPayableLoanAmount(loan.getPayableLoanAmount());
                    loanInfoDto.setUserEmail(email);
                    loanInfoDto.setMonthlyEMI(loan.getMonthlyEMI());
                    loanInfoDto.setFine(loan.getCurrentFine());
                    loanInfoDto.setStartDate(loan.getStartDate());
                    loanInfoDto.setRePaymentTerm(loan.getRePaymentTerm());
                } else
                    return new ResponseEntity<>("No active loan on your account", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(loanInfoDto,HttpStatus.OK);
        }
    }

    public static boolean verifyLoanType(String test) {
        for (LoanType loanType : LoanType.values()) {
            if (loanType.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public ResponseEntity<?> getLoanInfoByLoanType(LoanType loanType,double principalAmount, int rePaymentTerm) {

        if(verifyLoanType(loanType.toString())) {
            LoanTypeBasedInfoDto loanInfoDto = new LoanTypeBasedInfoDto();
            loanInfoDto.setPrincipalAmount(principalAmount);
            loanInfoDto.setRePaymentTerm(rePaymentTerm);
            loanInfoDto.setLoanType(loanType);
            loanInfoDto.setInterestRate(loanType.getLoanInterestRate());
            LoanCalcDto loanCalcDto = new LoanCalcDto();
            loanCalcDto.setLoanType(loanType);
            loanCalcDto.setPrincipalLoanAmount(principalAmount);
            loanCalcDto.setRePaymentTerm(rePaymentTerm);
            loanInfoDto.setMonthlyEMI(calculateEMI(loanCalcDto));
            loanInfoDto.setTotalPayableAmount(calculateFirstPayableAmount(loanCalcDto));
            return new ResponseEntity<>(loanInfoDto, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Override
    public ResponseEntity<?> getLoanInfoForOtherLoanType(double discount, double principalAmount, int rePaymentTerm) {
        EmiDetails emiDetails = emiService.calculateEmi(principalAmount, discount,rePaymentTerm);
        LoanTypeBasedInfoDto loanInfoDto = new LoanTypeBasedInfoDto();
        loanInfoDto.setTotalPayableAmount(emiDetails.getCustomerPrice());
        loanInfoDto.setDiscount(emiDetails.getDiscount());
        loanInfoDto.setMonthlyEMI(emiDetails.getEmi9Months());
        if (emiDetails.getEmi9Months() == 0) {
            loanInfoDto.setRePaymentTerm(12);
        } else if (emiDetails.getEmi12Months() == 0) {
            loanInfoDto.setRePaymentTerm(9);
        }
        return new ResponseEntity<>(loanInfoDto,HttpStatus.OK);
    }
    @Override
    public MonthlyEmiDto payEMI(String email) {
        User user = userService.getByEmail(email);
        if (accountsService.CheckAccStatus(user.getEmail()) == Boolean.FALSE)  return null;
        Accounts acc = user.getAccounts();
        MonthlyEmiDto monthlyEmiDto = new MonthlyEmiDto();
        List<Loan> loanList = acc.getLoanList();
        for (Loan loan : loanList) {
            if(loan.getStatus().equals(LoanStatus.SANCTIONED)) {
                if (penaltyService.noOfMonthsEmiMissed(loan.getLoanId()) == 0) {
                    double payableLoanAmount = loan.getPayableLoanAmount();
                    double temp = payableLoanAmount;
                    payableLoanAmount = temp - loan.getMonthlyEMI();
                    loan.setPayableLoanAmount(payableLoanAmount);
                    loan.setEmiDate(firstDateOfNextMonth(LocalDate.now()));

                    //transaction part
                    Transactions transactions = new Transactions();
                    transactions.setAccount(acc);
                    transactions.setLoan(loan);
                    transactions.setTransactionAmount(loan.getMonthlyEMI());
                    Transactions.addTotalBalance(loan.getMonthlyEMI());
                    transactions.setTransactionDate(new Date());
                    transactions.setTransactionType(TransactionType.CREDITED);
                    transactions.setTransactionStatus(TransactionStatus.COMPLETED);
                    transactionRepo.save(transactions);
                    loan.getTransactionsList().add(transactions);

                    LocalDate endDate = ChronoUnit.DAYS.addTo(loan.getStartDate(), loan.getRePaymentTerm());
                    int rePaymentTermLeft = (int) ChronoUnit.DAYS.between(endDate, LocalDate.now());
                    loanRepository.save(loan);

                    monthlyEmiDto.setPayableLoanAmount(payableLoanAmount);
                    monthlyEmiDto.setMonthlyEMI(loan.getMonthlyEMI());
                    monthlyEmiDto.setRePaymentTermLeft(rePaymentTermLeft);
                    monthlyEmiDto.setPaymentDate(LocalDate.now());
                    monthlyEmiDto.setNextEMIDate(firstDateOfNextMonth(LocalDate.now()));
                } else {
                    return payEMIWithFine(email);
                }
            }else {
                return null;
            }
        }
        return null;
    }

    private MonthlyEmiDto payEMIWithFine(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        MonthlyEmiDto monthlyEmiDto = new MonthlyEmiDto();
        List<Loan> loanList = acc.getLoanList();
        for (Loan loan : loanList) {
            Penalty penalty = penaltyService.chargePenaltyForLoan(loan.getLoanId());
            double payableLoanAmount = loan.getPayableLoanAmount();
            double temp = payableLoanAmount;
            payableLoanAmount = temp - loan.getMonthlyEMI();

            loan.setPayableLoanAmount(payableLoanAmount);
            loan.setEmiDate(firstDateOfNextMonth(LocalDate.now()));
            penalty.setPenaltyStatus(PenaltyStatus.PAID);
            penaltyRepo.save(penalty);

            Transactions transactions = new Transactions();
            transactions.setAccount(acc);
            transactions.setLoan(loan);
            transactions.setTransactionAmount(loan.getMonthlyEMI()+penalty.getPenaltyAmount());
            Transactions.addTotalBalance(loan.getMonthlyEMI()+penalty.getPenaltyAmount());
            transactions.setTransactionDate(new Date());
            transactions.setTransactionType(TransactionType.CREDITED);
            transactions.setTransactionStatus(TransactionStatus.COMPLETED);
            transactionRepo.save(transactions);
            loan.getTransactionsList().add(transactions);
            loan.setCurrentFine(0);
            loanRepository.save(loan);

            LocalDate endDate = ChronoUnit.DAYS.addTo(loan.getStartDate(), loan.getRePaymentTerm());
            int rePaymentTermLeft = (int) ChronoUnit.DAYS.between(endDate, LocalDate.now());

            monthlyEmiDto.setPayableLoanAmount(payableLoanAmount);
            monthlyEmiDto.setMonthlyEMI(loan.getMonthlyEMI() + loan.getCurrentFine());
            monthlyEmiDto.setRePaymentTermLeft(rePaymentTermLeft);
            monthlyEmiDto.setPaymentDate(LocalDate.now());
            monthlyEmiDto.setNextEMIDate(firstDateOfNextMonth(LocalDate.now()));
        }
        return monthlyEmiDto;
    }
    @Override
    public ResponseEntity<?> getLoanClosureDetails(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        LoanClosureDto loanClosureDto = new LoanClosureDto();
        List<Loan> loanList = acc.getLoanList();
        if (loanList.isEmpty())
            return new ResponseEntity<>("no Loan found", HttpStatus.NOT_FOUND);
        else {
            for (Loan loan : loanList) {
                if (isLoanNotOpen(email) == Boolean.FALSE) {
                    double monthlyEMI = loan.getMonthlyEMI();
                    loanClosureDto.setStatus(LoanStatus.REQUESTEDFORFORECLOSURE);
                    loanClosureDto.setLoanType(loan.getLoanType());
                    loanClosureDto.setFine(monthlyEMI / 100);
                    loanClosureDto.setPrincipalLoanAmount(loan.getPrincipalLoanAmount());
                    loanClosureDto.setLastEMIDate(firstDateOfNextMonth(LocalDate.now()));
                    loanClosureDto.setStartDate(loan.getStartDate());
                    loanClosureDto.setMonthlyEMI(loan.getPayableLoanAmount() + monthlyEMI / 100);
                    loanClosureDto.setRePaymentTerm((int) ChronoUnit.DAYS.between(loan.getStartDate(), firstDateOfNextMonth(LocalDate.now())));
                } else
                    return new ResponseEntity<>("no active loan", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(loanClosureDto, HttpStatus.OK);
        }
    }

    public ResponseEntity<?> applyForLoanClosure(String email) {
        User user = userService.getByEmail(email);
        Accounts acc = user.getAccounts();
        List<Loan> loanList = acc.getLoanList();
        if (loanList.isEmpty())
            return new ResponseEntity<>("no Loan record found", HttpStatus.NOT_FOUND);
        else {
            for (Loan loan : loanList) {
                if (isLoanNotOpen(email) == Boolean.FALSE) {
                    if (loan.getStatus().equals(LoanStatus.APPROVED) || loan.getStatus().equals(LoanStatus.SANCTIONED)) {
                        double monthlyEMI = loan.getMonthlyEMI();
                        loan.setStatus(LoanStatus.REQUESTEDFORFORECLOSURE);
                        loan.setCurrentFine(monthlyEMI / 100);
                        loan.setMonthlyEMI(loan.getPayableLoanAmount() + monthlyEMI / 100);
                        loan.setRePaymentTerm((int) ChronoUnit.DAYS.between(loan.getStartDate(), firstDateOfNextMonth(LocalDate.now())));
                    } else
                        return new ResponseEntity<>("No Approved/Sanctioned Loan Found", HttpStatus.NOT_FOUND);
                } else {
                    return new ResponseEntity<>("No running loan found", HttpStatus.NOT_FOUND);
                }
            }
            return new ResponseEntity<> ("Applied For Closure", HttpStatus.OK);
        }
    }
    public LocalDate firstDateOfNextMonth(LocalDate date) {
        LocalDate nextMonth = date.plusMonths(1);
        return nextMonth.withDayOfMonth(1);
    }
    @Override
    public ResponseEntity<?> findRateByLoanType(LoanType loanType){
        return new ResponseEntity<>(loanType+" - "+loanType.getLoanInterestRate(),HttpStatus.FOUND);
    }
}
