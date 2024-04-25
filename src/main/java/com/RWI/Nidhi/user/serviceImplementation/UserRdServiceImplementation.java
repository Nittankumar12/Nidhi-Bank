package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.entity.RecurringDeposit;
import com.RWI.Nidhi.user.repository.RecurringDepositRepo;
import com.RWI.Nidhi.user.serviceInterface.UserRdServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class UserRdServiceImplementation implements UserRdServiceInterface {

    @Autowired
    private RecurringDepositRepo rdRepo;

    @Override
    public RecurringDeposit createRd(RdDto rdDto) {
        RecurringDeposit rd = new RecurringDeposit();
        rd.setMonthlyDepositAmount(rdDto.getMonthlyDepositAmount());
        rd.setStartDate(LocalDate.now());
        rd.setTenure(rdDto.getTenure());
        rd.setMaturityDate(LocalDate.now().plusYears(rdDto.getTenure()));

        return null;
    }

    @Override
    public Double closeRd(int rdId) {
        return null;
    }

    @Override
    public Double onMaturity(Double amount, Integer tenure, Double interestRate) {
        return null;
    }
}
