package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.dto.RdRequestDto;
import com.RWI.Nidhi.entity.RecurringDeposit;

import java.util.List;

public interface UserRdServiceInterface {

    RdRequestDto createRd(String agentEmail, String email, RdDto rdDto);

    RecurringDeposit closeRd(int rdId);

    RdDto getRdById(int rdId);

    List<RdDto> getRdByEmail(String email);

//    Double onMaturity(Double amount, Integer tenure, Double interestRate);
}
