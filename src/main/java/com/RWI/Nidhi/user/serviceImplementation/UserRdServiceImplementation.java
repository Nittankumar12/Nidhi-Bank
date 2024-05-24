package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.dto.RdRequestDto;
import com.RWI.Nidhi.dto.RdResponseDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.CommissionType;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.enums.TransactionStatus;
import com.RWI.Nidhi.enums.TransactionType;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.user.serviceInterface.AccountsServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserRdServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserRdServiceImplementation implements UserRdServiceInterface {

    private final int penalty = 500;
    double currentInterest;
    @Autowired
    private RecurringDepositRepo rdRepo;
    @Autowired
    CommissionRepository commissionRepo;
    @Autowired
    private AgentRepo agentRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AccountsRepo accountRepo;
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    AccountsServiceInterface accountsService;

    @Override
    public RdResponseDto createRd(String agentEmail, String email, RdDto rdDto) {
        Agent agent = agentRepo.findByAgentEmail(agentEmail);
        User user = userRepo.findByEmail(email);
        if (accountsService.CheckAccStatus(user.getEmail()) == Boolean.FALSE) return null;
        Accounts accounts = new Accounts();
        RecurringDeposit rd = new RecurringDeposit();
        if (agent != null && user != null) {
            rd.setMonthlyDepositAmount(rdDto.getMonthlyDepositAmount());
            rd.setStartDate(LocalDate.now());
            rd.setTenure(rdDto.getTenure());
            rd.setNomineeName(rdDto.getNomineeName());
            rd.setInterestRate(rdDto.getRdCompoundingFrequency().getRdInterestRate());
            rd.setCompoundingFrequency(rdDto.getRdCompoundingFrequency().getCompoundingFreq());
            rd.setTotalAmountDeposited(calculateTotalAmount(rdDto.getMonthlyDepositAmount(), rdDto.getTenure()));
            rd.setMaturityDate(LocalDate.now().plusYears(rdDto.getTenure()));
            int tenureInDays = getCompleteDaysCount(rd.getStartDate(), rd.getMaturityDate());

            rd.setPenalty(0);
            rd.setMaturityAmount(calculateRdAmount(rd.getMonthlyDepositAmount(), rd.getInterestRate(), rd.getMaturityDate()));
            rd.setRdStatus(Status.ACTIVE);
            rd.setAgent(agent);
            rd.setAccount(user.getAccounts());

            Transactions transactions = new Transactions();
            transactions.setTransactionDate(new Date());
            transactions.setTransactionType(TransactionType.CREDITED);
            transactions.setTransactionAmount(rdDto.getMonthlyDepositAmount());
            transactions.setTransactionStatus(TransactionStatus.COMPLETED);
            transactions.setAccount(user.getAccounts());
            transactions.setRd(rd);
            Transactions.addTotalBalance(rdDto.getMonthlyDepositAmount());
            transactionRepo.save(transactions);
            accounts.getTransactionsList().add(transactions);
            accountRepo.save(accounts);

            //Commission
            Commission commission = new Commission();
            commission.setAgent(agent);
            commission.setUser(user);
            commission.setCommissionType((CommissionType.RD));
            commission.setCommissionRate(CommissionType.RD.getCommissionRate());
            commission.setCommissionAmount(accountsService.amountCalc(CommissionType.RD.getCommissionRate(),rd.getMaturityAmount()));
            commission.setCommDate(LocalDate.now());
            commissionRepo.save(commission);
            userRepo.save(user);
            agentRepo.save(agent);

            rd.getTransactionsList().add(transactions);
            rdRepo.save(rd);

            RdResponseDto rdResponseDto = new RdResponseDto();
            rdResponseDto.setUserName(rd.getAccount().getUser().getUserName());
            rdResponseDto.setNomineeName(rd.getNomineeName());
            rdResponseDto.setInterestRate(rd.getInterestRate());
            rdResponseDto.setMonthlyDepositAmount(rd.getMonthlyDepositAmount());
            rdResponseDto.setTenure(rd.getTenure());
            rdResponseDto.setStartDate(rd.getStartDate());
            rdResponseDto.setCompoundingFrequency(rd.getCompoundingFrequency());
            rdResponseDto.setMaturityAmount(rd.getMaturityAmount());
            rdResponseDto.setMaturityDate(rd.getMaturityDate());
            rdResponseDto.setRdStatus(rd.getRdStatus());
            rdResponseDto.setAgentName(rd.getAgent().getAgentName());

            return rdResponseDto;
        }
        return null;
    }


    private int getCompleteDaysCount(LocalDate startDate, LocalDate endDate) {
        double daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
        return (int) daysDifference;
    }

    private double calculateRdAmount(double monthlyDepositAmount, double interestRate, LocalDate maturityDate) {
        double currentAmount = 0;
        double ratePerMonth = interestRate / 365;
        int numberOfDays = getCompleteDaysCount(LocalDate.now(), maturityDate);

        for (int i = 1; i <= numberOfDays; i++) {
            currentAmount += monthlyDepositAmount;
//            System.out.print(currentAmount + "    ");
            double currInterest = (currentAmount * ratePerMonth) / 100;
//            System.out.print(currInterest + "    ");
            currentAmount += currInterest;
//            System.out.println(currentAmount);
        }
        return currentAmount;
    }

    private double calculateTotalAmount(double monthlyDepositAmount, int tenure) {
        double totalAmount = monthlyDepositAmount * tenure;
        return totalAmount;
    }


    @Override
    public RecurringDeposit closeRd(int rdId) throws Exception{
        RecurringDeposit currRd = rdRepo.findById(rdId).orElseThrow(() -> {
            return new Exception("RD not found");
        });
        if (currRd != null) {
            RecurringDeposit rd = currRd;
            if (rd.getMaturityAmount() == 0) {
                double interest = calculateRdAmount(rd.getMonthlyDepositAmount(), rd.getInterestRate(), rd.getMaturityDate());
                rd.setMaturityAmount(rd.getMonthlyDepositAmount() * rd.getTenure() + interest);
            }
            rd.setRdStatus(Status.CLOSED);

            Transactions transactions = new Transactions();
            transactions.setTransactionDate(new Date());
            transactions.setTransactionType(TransactionType.DEBITED);
            transactions.setTransactionAmount(currRd.getTotalAmountDeposited());
            transactions.setTransactionStatus(TransactionStatus.COMPLETED);
            transactions.setAccount(currRd.getAccount());
            transactions.setRd(currRd);
            Transactions.deductTotalBalance(currRd.getMonthlyDepositAmount());
            transactionRepo.save(transactions);
            currRd.getAccount().getTransactionsList().add(transactions);
            currRd.getTransactionsList().add(transactions);
            accountRepo.save(currRd.getAccount());
            rdRepo.save(rd);
            return rd;
        } else {
            throw new EntityNotFoundException("Recurring Deposit not found with ID: " + rdId);
        }
    }

    @Override
    public RdRequestDto getRdById(int rdId) throws Exception {
        RecurringDeposit recurringDeposit = rdRepo.findById(rdId)
                .orElseThrow(() -> new EntityNotFoundException("RD not found"));
        RdRequestDto responseDto = new RdRequestDto();
        responseDto.setRdId(rdId);
        responseDto.setUserName(recurringDeposit.getAccount().getUser().getUserName());
        responseDto.setNomineeName(recurringDeposit.getNomineeName());
        responseDto.setMonthlyDepositAmount(recurringDeposit.getMonthlyDepositAmount());
        responseDto.setTenure(recurringDeposit.getTenure());
        responseDto.setAgentName(recurringDeposit.getAgent().getAgentName());
        responseDto.setRdStatus(recurringDeposit.getRdStatus());
        return responseDto;
    }

    @Override
    public ResponseEntity<?> sendMonthlyIncomeToUser(int rdId) throws Exception{
        RecurringDeposit currRd = rdRepo.findById(rdId).orElseThrow(() -> {return new Exception("RD not found");});
        Transactions transactions = new Transactions();
        transactions.setTransactionDate(new Date());
        transactions.setTransactionType(TransactionType.DEBITED);
        transactions.setTransactionAmount(currRd.getMonthlyDepositAmount());
        transactions.setTransactionStatus(TransactionStatus.COMPLETED);
        transactions.setAccount(currRd.getAccount());
        transactions.setRd(currRd);
        Transactions.deductTotalBalance(currRd.getMonthlyDepositAmount());
        try {
            transactionRepo.save(transactions);
            rdRepo.save(currRd);
        }
        catch (Exception e){
            throw new Exception("Error");
        }
        return new ResponseEntity<>(currRd.getMonthlyDepositAmount(), HttpStatus.OK);
    }


    @Override
    public List<RdRequestDto> getRdByEmail(String email) {
        User user = userRepo.findByEmail(email);
        List<RecurringDeposit> recurringDepositList = user.getAccounts().getRecurringDepositList();
        List<RdRequestDto> rdRequestDtoList = new ArrayList<RdRequestDto>();
        for (RecurringDeposit recurringDeposit : recurringDepositList) {
            RdRequestDto responseDto = new RdRequestDto();
            responseDto.setUserName(user.getUserName());
            responseDto.setRdId(recurringDeposit.getRdId());
            responseDto.setMonthlyDepositAmount(recurringDeposit.getMonthlyDepositAmount());
            responseDto.setTenure(recurringDeposit.getTenure());
            responseDto.setNomineeName(recurringDeposit.getNomineeName());
            responseDto.setAgentName(user.getAgent().getAgentName());
            responseDto.setRdStatus(recurringDeposit.getRdStatus());

            rdRequestDtoList.add(responseDto);
        }
        return rdRequestDtoList;
    }
}
