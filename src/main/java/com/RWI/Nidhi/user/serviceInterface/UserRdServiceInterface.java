package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.entity.RecurringDeposit;
import java.util.List;
import java.util.Optional;

public interface UserRdServiceInterface {

    RecurringDeposit createRd(RdDto rdDto);
    RecurringDeposit closeRd(int rdId);
    List<RecurringDeposit> getAllRds();
    Optional<RecurringDeposit> getRdById(int rdId);

    Double onMaturity(Double amount, Integer tenure, Double interestRate);
}
