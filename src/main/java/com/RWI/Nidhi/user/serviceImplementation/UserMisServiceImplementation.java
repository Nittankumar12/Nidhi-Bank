package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.MisDto;
import com.RWI.Nidhi.dto.MisRequestDto;
import com.RWI.Nidhi.dto.MisResponseDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.CommissionType;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.enums.TransactionStatus;
import com.RWI.Nidhi.enums.TransactionType;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.user.serviceInterface.AccountsServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserMisServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserMisServiceImplementation implements UserMisServiceInterface {
    @Autowired
    private MisRepo misRepo;
    @Autowired
    private AgentRepo agentRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private AccountsRepo accountsRepo;
    @Autowired
    AccountsServiceInterface accountsService;
    @Autowired
    CommissionRepository commissionRepo;

    @Override
    public MisResponseDto createMis(String agentEmail, String email, MisDto misDto) {

        Agent agent = agentRepo.findByAgentEmail(agentEmail);
        User user = userRepo.findByEmail(email);
        if (accountsService.CheckAccStatus(user.getEmail()) == Boolean.FALSE) return null;
//        Accounts accounts = new Accounts();
        MIS newMis = new MIS();
        if (agent != null && user != null) {
            newMis.setTotalDepositedAmount(misDto.getTotalDepositedAmount());
            newMis.setStartDate(LocalDate.now());
            newMis.setTenure(misDto.getMisTenure().getTenure());
            newMis.setNomineeName(misDto.getNomineeName());
            newMis.setInterestRate(misDto.getMisTenure().getInterestRate());
            newMis.setMaturityDate(LocalDate.now().plusYears(misDto.getMisTenure().getTenure()));
            newMis.setMonthlyIncome(calculateMisMonthlyIncome(newMis.getTotalDepositedAmount(), newMis.getInterestRate()));
            newMis.setStatus(Status.ACTIVE);

            newMis.setAgent(agent);
            newMis.setAccount(user.getAccounts());
            newMis.setTransactionsList(new ArrayList<>());
            misRepo.save(newMis);

            Transactions transactions = new Transactions();
            transactions.setTransactionDate(new Date());
            transactions.setTransactionType(TransactionType.CREDITED);
            transactions.setTransactionAmount(misDto.getTotalDepositedAmount());
            transactions.setTransactionStatus(TransactionStatus.COMPLETED);
            transactions.setAccount(user.getAccounts());
            transactions.setMis(newMis);
            Transactions.addTotalBalance(misDto.getTotalDepositedAmount());
            transactionRepo.save(transactions);
//            accounts.getTransactionsList().add(transactions);
//            accountsRepo.save(accounts);

            Commission commission = new Commission();
            commission.setAgent(agent);
            commission.setUser(user);
            commission.setCommissionType(CommissionType.MIS);
            commission.setCommissionRate(CommissionType.MIS.getCommissionRate());
            commission.setCommissionAmount(accountsService.amountCalc(CommissionType.MIS.getCommissionRate(),newMis.getTotalDepositedAmount()));
            commission.setCommDate(LocalDate.now());
            commissionRepo.save(commission);
            userRepo.save(user);
            agentRepo.save(agent);


            misRepo.save(newMis);

            MisResponseDto misResponseDto = new MisResponseDto();
            misResponseDto.setUserName(newMis.getAccount().getUser().getUserName());
            misResponseDto.setNomineeName(newMis.getNomineeName());
            misResponseDto.setInterestRate(newMis.getInterestRate());
            misResponseDto.setTotalDepositedAmount(newMis.getTotalDepositedAmount());
            misResponseDto.setTenure(newMis.getTenure());
            misResponseDto.setStartDate(newMis.getStartDate());
            misResponseDto.setMonthlyIncome(newMis.getMonthlyIncome());
            misResponseDto.setMaturityDate(newMis.getMaturityDate());
            misResponseDto.setMisStatus(newMis.getStatus());
            misResponseDto.setAgentName(newMis.getAgent().getAgentName());
            return misResponseDto;
        }
        return null;
    }

    private Double calculateMisMonthlyIncome(double totalAmount, double interestRatePerAnnum) {
        double interestPerMonth = (totalAmount * (interestRatePerAnnum / 12)) / 100;
        return interestPerMonth;
    }

    @Override
    public Double closeMis(int misId) throws Exception {
        MIS currMis = misRepo.findById(misId).orElseThrow(() -> {
            return new Exception("MIS not found");
        });
        currMis.setTotalInterestEarned(currMis.getTenure() * 12 * currMis.getMonthlyIncome());
        currMis.setClosingDate(LocalDate.now());
        currMis.setStatus(Status.CLOSED);

        Transactions transactions = new Transactions();
        transactions.setTransactionDate(new Date());
        transactions.setTransactionType(TransactionType.DEBITED);
        transactions.setTransactionAmount(currMis.getTotalDepositedAmount());
        transactions.setTransactionStatus(TransactionStatus.COMPLETED);
        transactions.setAccount(currMis.getAccount());
        transactions.setMis(currMis);
        Transactions.deductTotalBalance(currMis.getTotalDepositedAmount());
        transactionRepo.save(transactions);
        currMis.getAccount().getTransactionsList().add(transactions);
        currMis.getTransactionsList().add(transactions);
        accountsRepo.save(currMis.getAccount());
        misRepo.save(currMis);
        return currMis.getTotalInterestEarned();
    }

    @Override
    public MisRequestDto getMisById(int misId) throws Exception {
        MIS mis = misRepo.findById(misId)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));
        MisRequestDto misRequestDto = new MisRequestDto();
        misRequestDto.setUserName(mis.getAccount().getUser().getUserName());
        misRequestDto.setMisId(misId);
        misRequestDto.setNomineeName(mis.getNomineeName());
        misRequestDto.setTotalDepositedAmount(mis.getTotalDepositedAmount());
        misRequestDto.setMisTenure(misRequestDto.getMisTenure());
        misRequestDto.setAgentName(mis.getAgent().getAgentName());
        misRequestDto.setStatus(mis.getStatus());
        return misRequestDto;
    }

    @Override
    public ResponseEntity<?> sendMonthlyIncomeToUser(int misId) throws Exception{
        MIS currMis = misRepo.findById(misId).orElseThrow(() -> {return new Exception("Mis not found");});
        Transactions transactions = new Transactions();
        transactions.setTransactionDate(new Date());
        transactions.setTransactionType(TransactionType.DEBITED);
        transactions.setTransactionAmount(currMis.getMonthlyIncome());
        transactions.setTransactionStatus(TransactionStatus.COMPLETED);
        transactions.setAccount(currMis.getAccount());
        transactions.setMis(currMis);
        Transactions.deductTotalBalance(currMis.getMonthlyIncome());
        try {
            transactionRepo.save(transactions);
            misRepo.save(currMis);
        }
        catch (Exception e){
            throw new Exception("Error");
        }
        return new ResponseEntity<>(currMis.getMonthlyIncome(), HttpStatus.OK);
    }


    @Override
    public List<MisRequestDto> getMisByEmail(String email) {
        User user = userRepo.findByEmail(email);
        List<MIS> misList = user.getAccounts().getMisList();
        List<MisRequestDto> misRequestDtoList = new ArrayList<MisRequestDto>();
        for (MIS mis : misList) {
            MisRequestDto misRequestDto = new MisRequestDto();
            misRequestDto.setUserName(user.getUserName());
            misRequestDto.setMisId(mis.getMisId());
            misRequestDto.setTotalDepositedAmount(mis.getTotalDepositedAmount());
            misRequestDto.setMisTenure(misRequestDto.getMisTenure());
            misRequestDto.setNomineeName(mis.getNomineeName());
            misRequestDto.setAgentName(user.getAgent().getAgentName());
            misRequestDto.setStatus(mis.getStatus());

            misRequestDtoList.add(misRequestDto);
        }
        return misRequestDtoList;
    }

}
