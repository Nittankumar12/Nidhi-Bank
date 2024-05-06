package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.MisDto;
import com.RWI.Nidhi.entity.MIS;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.repository.MisRepo;
import com.RWI.Nidhi.user.serviceInterface.UserMisServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserMisServiceImplementation implements UserMisServiceInterface {
    @Autowired
    private MisRepo misRepo;

    @Override
    public MIS createMis(MisDto misDto) {
        MIS newMis = new MIS();
        newMis.setTotalDepositedAmount(misDto.getTotalDepositedAmount());
        newMis.setStartDate(LocalDate.now());
        newMis.setTenure(misDto.getMisTenure().getTenure());
        newMis.setNomineeName(misDto.getNomineeName());
        newMis.setInterestRate(misDto.getMisTenure().getInterestRate());
        newMis.setMaturityDate(LocalDate.now().plusYears(misDto.getMisTenure().getTenure()));
        newMis.setMonthlyIncome(calculateMisMonthlyIncome(newMis.getTotalDepositedAmount(), newMis.getInterestRate()));
        newMis.setStatus(Status.ACTIVE);
        misRepo.save(newMis);
        return newMis;
    }
    private Double calculateMisMonthlyIncome(double totalAmount, double interestRatePerAnnum){
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
}
