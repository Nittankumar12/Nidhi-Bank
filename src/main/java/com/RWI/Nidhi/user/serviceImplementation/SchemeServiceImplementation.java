package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.user.repository.SchemeRepo;
import com.RWI.Nidhi.user.serviceInterface.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class SchemeServiceImplementation implements SchemeService {
    @Autowired
    SchemeRepo schemeRepo;
    @Override
    public int findSchemeRemainingDays(int schemeId) {
        LocalDate startDate = schemeRepo.findStartDateBySchemeId(schemeId);
        int tenure = schemeRepo.findTenureBySchemeId(schemeId);
        LocalDate endDate = startDate.plus(tenure, ChronoUnit.DAYS);
        long temp = ChronoUnit.DAYS.between(endDate,LocalDate.now() );
        int remainingDays = (int)temp;
        return remainingDays;
    }

    @Override
    public double findLoanOnSchemeBasis(int schemeId) {
        double schemeLoan = schemeRepo.findMonthlyDepositAmountBySchemeId(schemeId) / 30 * findSchemeRemainingDays(schemeId);
        return schemeLoan;
    }
}
