package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.dto.FdRequestDto;
import com.RWI.Nidhi.dto.FdResponseDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.CommissionType;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.enums.TransactionStatus;
import com.RWI.Nidhi.enums.TransactionType;
import com.RWI.Nidhi.repository.*;
import com.RWI.Nidhi.user.serviceInterface.AccountsServiceInterface;
import com.RWI.Nidhi.user.serviceInterface.UserFdServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserFdServiceImplementation implements UserFdServiceInterface {

    private final int penalty = 500;
    @Autowired
    FixedDepositRepo fdRepo;
//    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
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
    public FdResponseDto createFd(String email, FdDto fdDto) {
//        Agent agent = agentRepo.findByReferralCode(agentReferralCode);
        User user = userRepo.findByEmail(email);
        if (accountsService.CheckAccStatus(user.getEmail()) == Boolean.FALSE) {return null;}
        else {
            if(user.getAccounts().getFdList()==null)user.getAccounts().setFdList(new ArrayList<>());
            if(user.getAgent().getFixedDepositList()==null)user.getAgent().setFixedDepositList(new ArrayList<>());
            FixedDeposit fd = new FixedDeposit();
            if ( user != null) {
                fd.setAmount(fdDto.getAmount());
                fd.setDepositDate(LocalDate.now());
                fd.setTenure(fdDto.getTenure());
                fd.setNomineeName(fdDto.getNomineeName());
                fd.setMaturityDate(LocalDate.now().plusYears(fdDto.getTenure()));
                fd.setCompoundingFrequency(fdDto.getFdCompoundingFrequency().getCompoundingFreq());
                fd.setInterestRate(fdDto.getFdCompoundingFrequency().getFdInterestRate());
                fd.setPenalty(0);
                int tenureInDays = getCompleteDaysCount(fd.getDepositDate(), fd.getMaturityDate());
                fd.setMaturityAmount(calculateFdAmount(fd.getAmount(), fd.getInterestRate(), fd.getCompoundingFrequency(), tenureInDays));
                fd.setFdStatus(Status.ACTIVE);

                fd.setAgent(user.getAgent());
                user.getAgent().getFixedDepositList().add(fd);
                fd.setAccount(user.getAccounts());
                fd.setTransactionsList(new ArrayList<>());

                //Commission
                Commission commission = new Commission();
                commission.setAgent(user.getAgent());
                commission.setUser(user);
                commission.setCommissionType((CommissionType.FD));
                commission.setCommissionRate(CommissionType.FD.getCommissionRate());
                commission.setCommissionAmount(accountsService.amountCalc(CommissionType.FD.getCommissionRate(), fd.getAmount()));
                commission.setCommDate(LocalDate.now());
                commissionRepo.save(commission);
                userRepo.save(user);
                agentRepo.save(user.getAgent());
                fdRepo.save(fd);
                accountRepo.save(user.getAccounts());

                Transactions transactions = new Transactions();
                transactions.setAccount(user.getAccounts());
                transactions.setTransactionAmount(fd.getAmount());
                Transactions.addTotalBalance(fd.getAmount());
                transactions.setTransactionDate(new Date());
                transactions.setTransactionType(TransactionType.CREDITED);
                transactions.setTransactionStatus(TransactionStatus.COMPLETED);
                transactions.setFd(fd);

                transactionRepo.save(transactions);
//                if(fd.getTransactionsList() == null) fd.setTransactionsList(new ArrayList<>());
                fd.getTransactionsList().add(transactions);
                fdRepo.save(fd);

                // Send notification to admin
                String notificationMessage = "User " + user.getUserName() + " has applied for a FD";
                simpMessagingTemplate.convertAndSend("/topic/admin", notificationMessage);
                // Send notification to user
                String notificationMsg = "User " + user.getUserName() + " has applied for a FD";
                simpMessagingTemplate.convertAndSend("/topic/user", notificationMsg);

                FdResponseDto fdResponseDto = new FdResponseDto();
                fdResponseDto.setUserName(fd.getAccount().getUser().getUserName());
                fdResponseDto.setNomineeName(fd.getNomineeName());
                fdResponseDto.setInterestRate(fd.getInterestRate());
                fdResponseDto.setAmount(fd.getAmount());
                fdResponseDto.setTenure(fd.getTenure());
                fdResponseDto.setDepositDate(fd.getDepositDate());
                fdResponseDto.setCompoundingFrequency(fd.getCompoundingFrequency());
                fdResponseDto.setMaturityAmount(fd.getMaturityAmount());
                fdResponseDto.setMaturityDate(fd.getMaturityDate());
                fdResponseDto.setFdStatus(fd.getFdStatus());
                fdResponseDto.setAgentName(fd.getAgent().getAgentName());

                return fdResponseDto;
            }
            return null;
        }
    }


    private double calculateFdAmount(int amount, double interestRate, int compoundingFreq, int tenureInDays) {
        double finalAmount;
        finalAmount = amount * (Math.pow((1 + (interestRate / (100 * compoundingFreq))), ((tenureInDays / 365) * compoundingFreq)));
        return finalAmount;
    }

    private int getCompleteDaysCount(LocalDate startDate, LocalDate endDate) {
        double daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
        return (int) daysDifference;
    }


    @Override
    public Double closeFd(int fdId) throws Exception {
        FixedDeposit fd = fdRepo.findById(fdId).orElseThrow(() -> {
            return new Exception("FD not found");
        });
        fd.setMaturityAmount(calculateFdAmount(fd.getAmount(), fd.getInterestRate(), fd.getCompoundingFrequency(), getCompleteDaysCount(fd.getDepositDate(), LocalDate.now())));
        fd.setClosingDate(LocalDate.now());
        if (fd.getClosingDate().isBefore(fd.getMaturityDate())) {
            fd.setPenalty(this.penalty);
            fd.setFdStatus(Status.FORECLOSED);
        } else {
            fd.setFdStatus(Status.CLOSED);
            Transactions transactions = new Transactions();
            transactions.setTransactionDate(new Date());
            transactions.setTransactionType(TransactionType.DEBITED);
            transactions.setTransactionAmount(fd.getAmount());
            transactions.setTransactionStatus(TransactionStatus.COMPLETED);
            transactions.setAccount(fd.getAccount());
            transactions.setFd(fd);
            Transactions.deductTotalBalance(fd.getAmount());
            transactionRepo.save(transactions);
            fd.getAccount().getTransactionsList().add(transactions);
            fd.getTransactionsList().add(transactions);
            accountRepo.save(fd.getAccount());
//            fdRepo.save(fd);
        }
        fd.setMaturityAmount(fd.getMaturityAmount() - fd.getPenalty());
        fdRepo.save(fd);
        return fd.getMaturityAmount();
    }


    @Override
    public FdRequestDto getFdById(int fdId) throws Exception {
        FixedDeposit fixedDeposit = fdRepo.findById(fdId)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));
        FdRequestDto fdRequestDto = new FdRequestDto();
        fdRequestDto.setFdId(fdId);
        fdRequestDto.setNomineeName(fixedDeposit.getNomineeName());
        fdRequestDto.setAmount(fixedDeposit.getAmount());
        fdRequestDto.setTenure(fixedDeposit.getTenure());
        fdRequestDto.setUserName(fixedDeposit.getAccount().getUser().getUserName());
        fdRequestDto.setAgentName(fixedDeposit.getAgent().getAgentName());
        fdRequestDto.setFdStatus(fixedDeposit.getFdStatus());

        return fdRequestDto;
    }

    @Override
    public List<FdRequestDto> getFdByEmail(String email) {
        User user = userRepo.findByEmail(email);
        List<FixedDeposit> fixedDepositList = user.getAccounts().getFdList();
        List<FdRequestDto> fdRequestDtoList = new ArrayList<FdRequestDto>();
        for (FixedDeposit fixedDeposit : fixedDepositList) {
            FdRequestDto fdRequestDto = new FdRequestDto();
            fdRequestDto.setFdId(fixedDeposit.getFdId());
            fdRequestDto.setUserName(user.getUserName());
            fdRequestDto.setAmount(fixedDeposit.getAmount());
            fdRequestDto.setTenure(fixedDeposit.getTenure());
            fdRequestDto.setNomineeName(fixedDeposit.getNomineeName());
            fdRequestDto.setAgentName(user.getAgent().getAgentName());
            fdRequestDto.setFdStatus(fixedDeposit.getFdStatus());

            fdRequestDtoList.add(fdRequestDto);
        }
        return fdRequestDtoList;
    }
}
