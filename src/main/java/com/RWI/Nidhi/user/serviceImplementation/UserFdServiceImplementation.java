package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.entity.FixedDeposit;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.repository.FixedDepositRepo;
import com.RWI.Nidhi.user.serviceInterface.UserFdServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class UserFdServiceImplementation implements UserFdServiceInterface {

    private final int penalty = 500;
    @Autowired
    FixedDepositRepo fdRepo;

    @Override
    public FixedDeposit createFd(FdDto fdDto) {

        System.out.println(fdDto.getAmount());
        System.out.println(fdDto.getTenure());
        System.out.println(fdDto.getFdCompoundingFrequency().getCompoundingFreq());
        System.out.println(fdDto.getFdCompoundingFrequency().getFdInterestRate());

        FixedDeposit fd = new FixedDeposit();
        fd.setAmount(fdDto.getAmount());
        fd.setDepositDate(LocalDate.now());
        fd.setTenure(fdDto.getTenure());
        fd.setMaturityDate(LocalDate.now().plusYears(fdDto.getTenure()));
        fd.setCompoundingFrequency(fdDto.getFdCompoundingFrequency().getCompoundingFreq());
        fd.setInterestRate(fdDto.getFdCompoundingFrequency().getFdInterestRate());
        fd.setPenalty(0);
        int tenureInDays = getCompleteDaysCount(fd.getDepositDate(), fd.getMaturityDate());
        fd.setMaturityAmount(calculateFdAmount(fd.getAmount(), fd.getInterestRate(), fd.getCompoundingFrequency(), tenureInDays));
        fd.setFdStatus(Status.ACTIVE);
        return fdRepo.save(fd);
    }

    private double calculateFdAmount(int amount, double interestRate, int compoundingFreq, int tenureInDays) {
        double finalAmount;

        finalAmount = amount * (Math.pow((1 + (interestRate / (100 * compoundingFreq))), ((tenureInDays / 365) * compoundingFreq)));
        return 0;
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
}
