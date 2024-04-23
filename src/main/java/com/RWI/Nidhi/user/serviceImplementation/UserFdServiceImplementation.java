package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.entity.FixedDeposit;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.user.repository.FixedDepositRepo;
import com.RWI.Nidhi.user.serviceInterface.UserFdServiceInterface;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public class UserFdServiceImplementation implements UserFdServiceInterface {

    @Autowired
    FixedDepositRepo fdRepo;
    @Override
    public FixedDeposit createFd(FdDto fdDto) {

        FixedDeposit fd = new FixedDeposit();

        fd.setAmount(fdDto.getAmount());
        fd.setDepositDate(LocalDate.now());
        fd.setTenure(fdDto.getTenure());
        fd.setMaturityDate(LocalDate.now().plusYears(fdDto.getTenure()));
        fd.setCompoundingFrequency(fdDto.getFdCompoundingFrequency().getCompoundingFreq());
        fd.setInterestRate(fdDto.getFdCompoundingFrequency().getFdInterestRate());

        fd.setMaturityAmount(calculateFdAmount(fd.getAmount(), fd.getInterestRate(), fd.getCompoundingFrequency(), fd.getTenure()));
        fd.setStatus(Status.ACTIVE);

        return fdRepo.save(fd);

    }
    private double calculateFdAmount(int amount, double interestRate, int compoundingFreq, int tenureInYears){
        double finalAmount;

        finalAmount = amount * (Math.pow((1 + (interestRate / (100 * compoundingFreq))),(tenureInYears * compoundingFreq)));
        return finalAmount;
    }
}
