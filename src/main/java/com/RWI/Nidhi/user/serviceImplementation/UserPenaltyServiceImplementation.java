package com.RWI.Nidhi.user.serviceImplementation;

import com.RWI.Nidhi.entity.Loan;
import com.RWI.Nidhi.entity.Penalty;
import com.RWI.Nidhi.enums.PenaltyStatus;
import com.RWI.Nidhi.repository.LoanRepo;
import com.RWI.Nidhi.repository.PenaltyRepo;
import com.RWI.Nidhi.user.serviceInterface.UserPenaltyServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
@Service
public class UserPenaltyServiceImplementation implements UserPenaltyServiceInterface {
    @Autowired
    private PenaltyRepo penaltyRepo;
    @Autowired
    private LoanRepo loanRepo;
    @Autowired
    private UserLoanServiceImplementation userLoanService;

    @Override
    public Penalty chargePenaltyForLoan(int loanId) {
        Loan currLoan = loanRepo.findById(loanId).orElseThrow(() -> new EntityNotFoundException("Loan not found"));
        if (LocalDate.now().isAfter(currLoan.getEmiDate())) {
            double penaltyAmount = calculatePenaltyAmount(currLoan.getLoanId());
            Penalty penalty = new Penalty();
            penalty.setPenaltyAmount(penaltyAmount);
            penalty.setDueDate(firstDateOfNextMonth(LocalDate.now()));
            penalty.setLoan(currLoan);
            penalty.setPenaltyStatus(PenaltyStatus.DUE);
            penaltyRepo.save(penalty);
            List<Penalty> penaltyList = new ArrayList<>();
            penaltyList.add(penalty);
            currLoan.setPenalty(penaltyList);
            currLoan.setCurrentFine(penaltyAmount);
            loanRepo.save(currLoan);
            return penalty;

        } else {
            return null;
        }
    }


        }
        else {
            return null;
        }
    }

    public double calculatePenaltyAmount(int loanId) {
        double penaltyAmount = 0;
        if (noOfMonthsEmiMissed(loanId) == 0)
            return penaltyAmount;
        else {
            double penaltyEMI = noOfMonthsEmiMissed(loanId) * loanRepo.findById(loanId).orElseThrow().getMonthlyEMI();
            double penaltyRate = 0.02;

            penaltyAmount = penaltyRate * penaltyEMI * noOfMonthsEmiMissed(loanId);

            penaltyAmount = penaltyRate*penaltyEMI*noOfMonthsEmiMissed(loanId);

            return penaltyAmount;
        }
    }


    public int noOfMonthsEmiMissed(int loanId) {//no of months on which penalty will apply on
        return (noOfMonthsEMIPaidAfterPenalty(loanId) - noOfMonthsEMIPaidBeforePenalty(loanId));
    }

    public int noOfMonthsEMIPaidBeforePenalty(int loanId) {

    public int noOfMonthsEmiMissed(int loanId){//no of months on which penalty will apply on
        return (noOfMonthsEMIPaidAfterPenalty(loanId) - noOfMonthsEMIPaidBeforePenalty(loanId));
    }
    public int noOfMonthsEMIPaidBeforePenalty(int loanId){

        //no of months before penalty has been paid
        Loan loan = loanRepo.findById(loanId).orElseThrow();
        double monthlyEMI = loan.getMonthlyEMI();
        double amountOfEMIPaid = loan.getPayableLoanAmount();//penalty has not been paid

        int noOfMonthsEMIPaidBeforePenalty = (int) (amountOfEMIPaid / monthlyEMI);
        return noOfMonthsEMIPaidBeforePenalty;
    }

    public int noOfMonthsEMIPaidAfterPenalty(int loanId) {
        // no of months after penalty has been paid i.e., no of months EMI should have been paid for
        Loan loan = loanRepo.findById(loanId).orElseThrow();
        LocalDate currentMonthDate = firstDateOfCurrentMonth(LocalDate.now());
        int noOfEMIPaidAfterPenalty = (int) ChronoUnit.MONTHS.between(currentMonthDate, loan.getStartDate());

        int noOfMonthsEMIPaidBeforePenalty = (int) (amountOfEMIPaid/monthlyEMI);
        return noOfMonthsEMIPaidBeforePenalty;
    }
    public int noOfMonthsEMIPaidAfterPenalty(int loanId){
        // no of months after penalty has been paid i.e., no of months EMI should have been paid for
        Loan loan = loanRepo.findById(loanId).orElseThrow();
        LocalDate currentMonthDate = firstDateOfCurrentMonth(LocalDate.now());
        int noOfEMIPaidAfterPenalty = (int) ChronoUnit.MONTHS.between(currentMonthDate,loan.getStartDate());

        return noOfEMIPaidAfterPenalty;
    }

    @Override
    public Penalty getPenaltyById(int penaltyId) {
        return penaltyRepo.findById(penaltyId).orElseThrow();
    }

    @Override
    public List<Penalty> getAllPenalties() {
        return penaltyRepo.findAll();
    }


    public LocalDate firstDateOfNextMonth(LocalDate date) {
        LocalDate nextMonth = date.plusMonths(1);
        return nextMonth.withDayOfMonth(1);
    }

    public LocalDate firstDateOfCurrentMonth(LocalDate date) {
        return date.withDayOfMonth(1);
    }
}
    public LocalDate firstDateOfCurrentMonth(LocalDate date) {
        return date.withDayOfMonth(1);
    }
}

