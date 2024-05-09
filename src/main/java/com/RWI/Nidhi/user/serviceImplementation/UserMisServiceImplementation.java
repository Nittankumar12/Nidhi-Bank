package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.MisDto;
import com.RWI.Nidhi.dto.MisRequestDto;
import com.RWI.Nidhi.entity.*;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.repository.AgentRepo;
import com.RWI.Nidhi.repository.MisRepo;
import com.RWI.Nidhi.repository.UserRepo;
import com.RWI.Nidhi.user.serviceInterface.UserMisServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserMisServiceImplementation implements UserMisServiceInterface {
    @Autowired
    private MisRepo misRepo;
    @Autowired
    private AgentRepo agentRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    public MisRequestDto createMis(String agentEmail, String email,MisDto misDto) {
        Agent agent = agentRepo.findByAgentEmail(agentEmail);
        User user = userRepo.findByEmail(email);
        Accounts accounts = new Accounts();
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

            misRepo.save(newMis);

            MisRequestDto misRequestDto = new MisRequestDto();
            misRequestDto.setUserName(newMis.getAccount().getUser().getUserName());
            misRequestDto.setNomineeName(newMis.getNomineeName());
            misRequestDto.setInterestRate(newMis.getInterestRate());
            misRequestDto.setTotalDepositedAmount(newMis.getTotalDepositedAmount());
            misRequestDto.setTenure(newMis.getTenure());
            misRequestDto.setStartDate(newMis.getStartDate());
            misRequestDto.setMonthlyIncome(newMis.getMonthlyIncome());
            misRequestDto.setMaturityDate(newMis.getMaturityDate());
            misRequestDto.setMisStatus(newMis.getStatus());
            misRequestDto.setAgentName(newMis.getAgent().getAgentName());
            return misRequestDto;
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
        misRepo.save(currMis);
        return currMis.getTotalInterestEarned();
    }

    @Override
    public List<MisDto> getMisByEmail(String email) {
        return null;
    }
}
