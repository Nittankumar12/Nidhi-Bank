package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.entity.RecurringDeposit;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.user.repository.RecurringDepositRepo;
import com.RWI.Nidhi.user.serviceInterface.UserRdServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
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
    public RecurringDeposit closeRd(int rdId) {
        Optional<RecurringDeposit> optionalRd = rdRepo.findById(rdId);
        if (optionalRd.isPresent()) {
            RecurringDeposit rd = optionalRd.get();
            if (rd.getMaturityAmount() == 0) {
                double interest = calculateInterest(rd);
                rd.setMaturityAmount(rd.getMonthlyDepositAmount() * rd.getTenure() + interest);
            }
            rd.setRdStatus(Status.CLOSED);
            rdRepo.save(rd);
            return rd;
        } else {
            throw new EntityNotFoundException("Recurring Deposit not found with ID: " + rdId);
        }
    }

    private double calculateInterest(RecurringDeposit rd) {
        double totalDepositAmount = rd.getMonthlyDepositAmount() * rd.getTenure();
        double interest = (totalDepositAmount * rd.getInterestRate() * rd.getTenure()) / 1200;
        return interest;
    }

//    @Override
//    public RecurringDeposit closeRd(int rdId) {
//        Optional<RecurringDeposit> optionalRd = recurringDepositRepository.findById(rdId);
//        if (optionalRd.isPresent()) {
//            RecurringDeposit rd = optionalRd.get();
//            if (rd.getMaturityAmount() == 0) {
//                double interest = calculateInterest(rd);
//                rd.setMaturityAmount(rd.getMonthlyDepositAmount() * rd.getTenure() + interest);
//            }
//            rd.setRdStatus(Status.CLOSED);
//            recurringDepositRepository.save(rd);
//            return rd;
//        } else {
//            throw new EntityNotFoundException("Recurring Deposit not found with ID: " + rdId);
//        }
//    }

    @Override
    public Double onMaturity(Double amount, Integer tenure, Double interestRate) {
        return null;
    }
}
