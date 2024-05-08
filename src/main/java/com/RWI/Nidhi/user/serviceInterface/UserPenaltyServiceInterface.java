package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.entity.Penalty;

import java.util.List;

public interface UserPenaltyServiceInterface {
    Penalty chargePenaltyForLoan(int loanId);

    Penalty getPenaltyById(int id);

    List<Penalty> getAllPenalties();

    double calculatePenaltyAmount(int loanId);
}