package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.dto.FdRequestDto;
import com.RWI.Nidhi.entity.Accounts;
import com.RWI.Nidhi.entity.Agent;
import com.RWI.Nidhi.entity.FixedDeposit;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.repository.AccountsRepo;
import com.RWI.Nidhi.repository.AgentRepo;
import com.RWI.Nidhi.repository.FixedDepositRepo;
import com.RWI.Nidhi.repository.UserRepo;
import com.RWI.Nidhi.user.serviceInterface.UserFdServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class UserFdServiceImplementation implements UserFdServiceInterface {

    private final int penalty = 500;
    @Autowired
    FixedDepositRepo fdRepo;
    @Autowired
    private AgentRepo agentRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AccountsRepo accountRepo;

    @Override
    public FdRequestDto createFd(String agentEmail, String email, FdDto fdDto) {
        Agent agent = agentRepo.findByAgentEmail(agentEmail);
        User user = userRepo.findUserByEmail(email).get();
        Accounts accounts = new Accounts();
        FixedDeposit fd = new FixedDeposit();
        if (agent != null && user != null) {
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

            fd.setAgent(agent);
            fd.setAccount(user.getAccounts());
            fdRepo.save(fd);

            FdRequestDto fdRequestDto = new FdRequestDto();
            fdRequestDto.setUserName(fd.getAccount().getUser().getUserName());
            fdRequestDto.setNomineeName(fd.getNomineeName());
            fdRequestDto.setInterestRate(fd.getInterestRate());
            fdRequestDto.setAmount(fd.getAmount());
            fdRequestDto.setTenure(fd.getTenure());
            fdRequestDto.setDepositDate(fd.getDepositDate());
            fdRequestDto.setCompoundingFrequency(fd.getCompoundingFrequency());
            fdRequestDto.setMaturityAmount(fd.getMaturityAmount());
            fdRequestDto.setMaturityDate(fd.getMaturityDate());
            fdRequestDto.setFdStatus(fd.getFdStatus());
            fdRequestDto.setAgentName(fd.getAgent().getAgentName());


            return fdRequestDto;
        }
        return null;
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
    public Double closeFd(int fdId) {
        FixedDeposit fd = fdRepo.findById(fdId).get();
        fd.setMaturityAmount(calculateFdAmount(fd.getAmount(), fd.getInterestRate(), fd.getCompoundingFrequency(), getCompleteDaysCount(fd.getDepositDate(), LocalDate.now())));
        fd.setClosingDate(LocalDate.now());
        if (fd.getClosingDate().isBefore(fd.getMaturityDate())) {
            fd.setPenalty(this.penalty);
            fd.setFdStatus(Status.FORECLOSED);
        } else {
            fd.setFdStatus(Status.CLOSED);
        }
        fd.setMaturityAmount(fd.getMaturityAmount() - fd.getPenalty());
        fdRepo.save(fd);
        return fd.getMaturityAmount();
    }

    @Override
    public List<FixedDeposit> getAllFds() {
        return fdRepo.findAll();
    }

    @Override
    public FdDto getFdById(int fdId) {
        FixedDeposit fixedDeposit = fdRepo.findById(fdId)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));
        FdDto fdDto = new FdDto();
        fdDto.setNomineeName(fixedDeposit.getNomineeName());
        fdDto.setAmount(fixedDeposit.getAmount());
        fdDto.setTenure(fixedDeposit.getTenure());
        return fdDto;
    }

}
