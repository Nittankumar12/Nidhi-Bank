package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.entity.Agent;
import com.RWI.Nidhi.entity.RecurringDeposit;
import com.RWI.Nidhi.entity.User;
import com.RWI.Nidhi.enums.Status;
import com.RWI.Nidhi.repository.AgentRepo;
import com.RWI.Nidhi.repository.RecurringDepositRepo;
import com.RWI.Nidhi.repository.UserRepo;
import com.RWI.Nidhi.user.serviceInterface.UserRdServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class UserRdServiceImplementation implements UserRdServiceInterface {

    private final int penalty = 500;
    double currentInterest;

    @Autowired
    private RecurringDepositRepo rdRepo;
    @Autowired
    private AgentRepo agentRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    public RecurringDeposit createRd(String agentEmail, String email, RdDto rdDto) {
        Optional<Agent> agent = agentRepo.findByEmail(agentEmail);
        Optional<User> user = userRepo.findUserByEmail(email);
        if (agent.isPresent() && user.isPresent()) {
            RecurringDeposit rd = new RecurringDeposit();
            rd.setMonthlyDepositAmount(rdDto.getMonthlyDepositAmount());
            rd.setStartDate(LocalDate.now());
            rd.setTenure(rdDto.getTenure());
            rd.setNomineeName(rdDto.getNomineeName());
            rd.setInterestRate(rdDto.getRdCompoundingFrequency().getRdInterestRate());
            rd.setCompoundingFrequency(rdDto.getRdCompoundingFrequency().getCompoundingFreq());
            rd.setTotalAmountDeposited(calculateTotalAmount(rdDto.getMonthlyDepositAmount(), rdDto.getTenure()));
            rd.setMaturityDate(LocalDate.now().plusYears(rdDto.getTenure()));
            int tenureInDays = getCompleteDaysCount(rd.getStartDate(), rd.getMaturityDate());
            rd.setPenalty(0);
            rd.setMaturityAmount(calculateRdAmount(rd.getMonthlyDepositAmount(), rd.getInterestRate(), rd.getMaturityDate()));
            rd.setRdStatus(Status.ACTIVE);
            return rdRepo.save(rd);
        }
        return null;
    }


    private int getCompleteDaysCount(LocalDate startDate, LocalDate endDate) {
        double daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
        return (int) daysDifference;
    }

    private double calculateRdAmount(double monthlyDepositAmount, double interestRate, LocalDate maturityDate) {
        double currentAmount = 0;
        double ratePerMonth = interestRate / 365;
        int numberOfDays = getCompleteDaysCount(LocalDate.now(), maturityDate);

        for (int i = 1; i <= numberOfDays; i++) {
            currentAmount += monthlyDepositAmount;
//            System.out.print(currentAmount + "    ");
            double currInterest = (currentAmount * ratePerMonth) / 100;
//            System.out.print(currInterest + "    ");
            currentAmount += currInterest;
//            System.out.println(currentAmount);
        }
        return currentAmount;
    }

    private double calculateTotalAmount(double monthlyDepositAmount, int tenure) {
        double totalAmount = monthlyDepositAmount * tenure;
        return totalAmount;
    }


    @Override
    public RecurringDeposit closeRd(int rdId) {
        Optional<RecurringDeposit> optionalRd = rdRepo.findById(rdId);
        if (optionalRd.isPresent()) {
            RecurringDeposit rd = optionalRd.get();
            if (rd.getMaturityAmount() == 0) {
                double interest = calculateRdAmount(rd.getMonthlyDepositAmount(), rd.getInterestRate(), rd.getMaturityDate());
                rd.setMaturityAmount(rd.getMonthlyDepositAmount() * rd.getTenure() + interest);
            }
            rd.setRdStatus(Status.CLOSED);
            rdRepo.save(rd);
            return rd;
        } else {
            throw new EntityNotFoundException("Recurring Deposit not found with ID: " + rdId);
        }
    }

    @Override
    public List<RecurringDeposit> getAllRds() {
        return rdRepo.findAll();
    }

    @Override
    public RdDto getRdById(int rdId) {
        RecurringDeposit recurringDeposit = rdRepo.findById(rdId)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));
        RdDto rdDto = new RdDto();
        rdDto.setNomineeName(recurringDeposit.getNomineeName());
        rdDto.setMonthlyDepositAmount(recurringDeposit.getMonthlyDepositAmount());
        rdDto.setTenure(recurringDeposit.getTenure());
        return rdDto;
    }

    @Override
    public Double onMaturity(Double amount, Integer tenure, Double interestRate) {
        return null;
    }
}
