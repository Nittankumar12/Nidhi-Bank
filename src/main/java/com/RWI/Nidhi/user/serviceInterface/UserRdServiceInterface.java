package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.entity.RecurringDeposit;

public interface UserRdServiceInterface {

    RecurringDeposit createRd(RdDto rdDto);
    RecurringDeposit closeRd(int rdId);

    Double onMaturity(Double amount, Integer tenure, Double interestRate);
}
