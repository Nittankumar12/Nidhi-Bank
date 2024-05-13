package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.dto.FdRequestDto;
import com.RWI.Nidhi.dto.FdResponseDto;
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
import java.util.ArrayList;
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
    public FdResponseDto createFd(String agentEmail, String email, FdDto fdDto) {
        Agent agent = agentRepo.findByAgentEmail(agentEmail);
        User user = userRepo.findByEmail(email);
//        Accounts accounts = new Accounts();
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
    public FdRequestDto getFdById(int fdId) {
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
