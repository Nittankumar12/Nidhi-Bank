package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.RdDto;
import com.RWI.Nidhi.dto.RdRequestDto;
import com.RWI.Nidhi.dto.RdResponseDto;
import com.RWI.Nidhi.entity.RecurringDeposit;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserRdServiceInterface {

    RdResponseDto createRd(String email, RdDto rdDto);

    RecurringDeposit closeRd(int rdId) throws Exception;

    RdRequestDto getRdById(int rdId) throws Exception;

    List<RdRequestDto> getRdByEmail(String email);

//    Double onMaturity(Double amount, Integer tenure, Double interestRate);
}
