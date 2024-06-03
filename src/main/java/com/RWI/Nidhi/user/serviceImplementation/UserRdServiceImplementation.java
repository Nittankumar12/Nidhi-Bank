package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.dto.RdRequestDto;
import com.RWI.Nidhi.dto.RdResponseDto;
import com.RWI.Nidhi.entity.Commission;
import com.RWI.Nidhi.entity.RecurringDeposit;
import com.RWI.Nidhi.entity.Transactions;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.*;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.user.serviceInterface.AccountsServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserRdServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    CommissionRepository commissionRepo;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    AgentRepo agentRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    AccountsRepo accountRepo;
    @Autowired
    TransactionRepo transactionRepo;
    @Autowired
    AccountsServiceInterface accountsService;
    @Autowired
    private RecurringDepositRepo rdRepo;

    @Override
    public RdResponseDto createRd(String email, RdDto rdDto) {
        User user = userRepo.findByEmail(email);
        if (accountsService.CheckAccStatus(user.getEmail()) == Boolean.FALSE) return null;
//        Accounts accounts = new Accounts();
        RecurringDeposit rd = new RecurringDeposit();

        if (user != null) {
            if (user.getAccounts().getRecurringDepositList() == null)
                user.getAccounts().setRecurringDepositList(new ArrayList<>());
            if (user.getAgent().getRecurringDepositList() == null)
                user.getAgent().setRecurringDepositList(new ArrayList<>());
            rd.setMonthlyDepositAmount(rdDto.getMonthlyDepositAmount());
            rd.setStartDate(LocalDate.now());
            rd.setTenure(rdDto.getTenure());
            rd.setNomineeName(rdDto.getNomineeName());
            rd.setInterestRate(rdDto.getRdCompoundingFrequency().getRdInterestRate());
            rd.setCompoundingFrequency(rdDto.getRdCompoundingFrequency().getCompoundingFreq());
            rd.setTotalAmountDeposited(calculateTotalAmount(rdDto.getMonthlyDepositAmount(), rdDto.getTenure()));
            rd.setMaturityDate(LocalDate.now().plusYears(rdDto.getTenure()));
//            int tenureInDays = getCompleteDaysCount(rd.getStartDate(), rd.getMaturityDate());

            rd.setPenalty(0);
            rd.setMaturityAmount(calculateRdAmount(rd.getMonthlyDepositAmount(), rd.getCompoundingFrequency(), rd.getInterestRate(), rd.getMaturityDate()));
            rd.setRdStatus(Status.ACTIVE);
            rd.setAgent(user.getAgent());
            rd.setAccount(user.getAccounts());
            rd.setTransactionsList(new ArrayList<>());
            rdRepo.save(rd);

            Transactions transactions = new Transactions();
            transactions.setTransactionDate(new Date());
            transactions.setTransactionType(TransactionType.CREDITED);
            transactions.setTransactionAmount(rdDto.getMonthlyDepositAmount());
            transactions.setTransactionStatus(TransactionStatus.COMPLETED);
            transactions.setAccount(user.getAccounts());
            transactions.setRd(rd);
            Transactions.addTotalBalance(rdDto.getMonthlyDepositAmount());
            transactionRepo.save(transactions);
//            accounts.getTransactionsList().add(transactions);
//            accountRepo.save(accounts);

            //Commission
            Commission commission = new Commission();
            commission.setAgent(user.getAgent());
            commission.setUser(user);
            commission.setCommissionType((CommissionType.RD));
            commission.setCommissionRate(CommissionType.RD.getCommissionRate());
            commission.setCommissionAmount(accountsService.amountCalc(CommissionType.RD.getCommissionRate(), rd.getMaturityAmount()));
            commission.setCommDate(LocalDate.now());
            commissionRepo.save(commission);
            userRepo.save(user);
            agentRepo.save(user.getAgent());
            accountRepo.save(user.getAccounts());

            rd.getTransactionsList().add(transactions);
            rdRepo.save(rd);
            // Send notification to admin
            String notificationMessage = "User " + user.getUserName() + " has applied for a RD";
            simpMessagingTemplate.convertAndSend("/topic/admin", notificationMessage);
            // Send notification to user
            String notificationMsg = "User " + user.getUserName() + " has applied for a RD";
            simpMessagingTemplate.convertAndSend("/topic/user", notificationMsg);

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

    private double calculateRdAmount(double monthlyDepositAmount, int compoundingFreq, double interestRate, LocalDate maturityDate) {

        long tenureInMonths = ChronoUnit.MONTHS.between(LocalDate.now(),maturityDate);
        double totalAmount = 0;
        double interestRatePerMonth = interestRate / 12;

        for (int i = 1; i <= tenureInMonths; i++) {
            double currInterest = 0;
            for (int j = i; j < i + (12 / compoundingFreq); j++) {
                totalAmount += monthlyDepositAmount;

                currInterest += (totalAmount * interestRatePerMonth / 100);
            }
            totalAmount += currInterest;
            i += 2;
        }
        return totalAmount;
    }


    private double calculateTotalAmount(double monthlyDepositAmount, int tenure) {
        double totalAmount = monthlyDepositAmount * tenure * 12;
        return totalAmount;
    }


    @Override
    public RecurringDeposit closeRd(int rdId) throws Exception {
        RecurringDeposit currRd = rdRepo.findById(rdId).orElseThrow(() -> {
            return new Exception("RD not found");
        });
        if (currRd != null) {
            RecurringDeposit rd = currRd;
            if (rd.getMaturityAmount() == 0) {
                double interest = calculateRdAmount(rd.getMonthlyDepositAmount(), rd.getCompoundingFrequency(), rd.getInterestRate(), rd.getMaturityDate());
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
