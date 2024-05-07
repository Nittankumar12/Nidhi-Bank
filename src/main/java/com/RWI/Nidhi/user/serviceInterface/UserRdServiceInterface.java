package com.RWI.Nidhi.user.serviceInterface;


import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.entity.RecurringDeposit;

import java.util.List;


public interface UserRdServiceInterface {

    RecurringDeposit createRd(String agentEmail, String email, RdDto rdDto);

    RecurringDeposit closeRd(int rdId);

    List<RecurringDeposit> getAllRds();

    RdDto getRdById(int rdId);

    Double onMaturity(Double amount, Integer tenure, Double interestRate);
}
