package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.FdDto;
import com.RWI.Nidhi.dto.FdRequestDto;
import com.RWI.Nidhi.entity.FixedDeposit;

import java.util.List;


public interface UserFdServiceInterface {
    FdRequestDto createFd(String agentEmail, String email, FdDto fdDto);

    Double closeFd(int fdId);

    List<FixedDeposit> getAllFds();

    FdDto getFdById(int fdId);

}
